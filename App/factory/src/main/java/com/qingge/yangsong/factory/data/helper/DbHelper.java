package com.qingge.yangsong.factory.data.helper;


import com.qingge.yangsong.factory.model.db.AppDatabase;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.GroupMember;
import com.qingge.yangsong.factory.model.db.Group_Table;
import com.qingge.yangsong.factory.model.db.Message;
import com.qingge.yangsong.factory.model.db.Session;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DbHelper {

    private final static DbHelper instance;

    static {
        instance = new DbHelper();
    }

    /**
     * 观察者的集合
     * Class<?>： 观察的表
     * Set<ChangedListener>：每一个表对应的观察者有很多
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 从所有的监听者中，获取某一个表的所有监听者
     *
     * @param modelClass 表对应的Class信息
     * @param <Model>    范型
     * @return Set<ChangedListener>
     */
    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass) {
        if (changedListeners.containsKey(modelClass)) {
            return changedListeners.get(modelClass);
        }
        return null;
    }

    /**
     * 添加一个监听
     *
     * @param tClass   对某个表关注
     * @param listener 监听者
     * @param <Model>  表的范型
     */
    public static <Model extends BaseModel> void addChangedListener(final Class<Model> tClass,
                                                                    ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            // 初始化某一类型的容器
            changedListeners = new HashSet<>();
            // 添加到中的Map
            instance.changedListeners.put(tClass, changedListeners);
        }
        changedListeners.add(listener);
    }


    /**
     * 删除某一个表的某一个监听器
     *
     * @param tClass   表
     * @param listener 监听器
     * @param <Model>  表的范型
     */
    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> tClass,
                                                                       ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            // 容器本身为null，代表根本就没有
            return;
        }
        // 从容器中删除你这个监听者
        changedListeners.remove(listener);
    }



    //数据库的添加或修改后的操作统一封装
    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;

        //拿到当前数据库的管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交事务
        definition.beginTransactionAsync(databaseWrapper -> {
            ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
            //数组转换成集合并保存
            adapter.saveAll(Arrays.asList(models));
            instance.notifySave(tClass,models);
        }).build().execute();
    }


    //数据库的删除操作统一封装
    public static <Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;

        //拿到当前数据库的管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);

        //提交事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                //删除
                adapter.deleteAll(Arrays.asList(models));
                instance.notifyDelete(tClass,models);
            }
        }).build().execute();
    }


    private <Model extends BaseModel> void notifySave(final Class<Model> tClass,
                                                       final Model... models){
        // 找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            // 通用的通知
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }

        // 列外情况
        if (GroupMember.class.equals(tClass)) {
            // 群成员变更，需要通知对应群信息更新
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            // 消息变化，应该通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    private <Model extends BaseModel> void notifyDelete(final Class<Model> tClass,
                                                      final Model... models){
        // 找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            // 通用的通知
            for (ChangedListener<Model> listener : listeners) {

                listener.onDataDelete(models);
            }
        }

        // 列外情况
        if (GroupMember.class.equals(tClass)) {
            // 群成员变更，需要通知对应群信息更新
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            // 消息变化，应该通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 从成员中找出成员对应的群，并对群进行更新
     *
     * @param members 群成员列表
     */
    private void updateGroup(GroupMember... members) {
        // 不重复集合
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            // 添加群Id
            groupIds.add(member.getGroup().getId());
        }

        // 异步的数据库查询，并异步的发起二次通知
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // 找到需要通知的群
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();

                // 调用直接进行一次通知分发
                instance.notifySave(Group.class, groups.toArray(new Group[0]));

            }
        }).build().execute();
    }

    /**
     * 从消息列表中，筛选出对应的会话，并对会话进行更新
     *
     * @param messages Message列表
     */
    private void updateSession(Message... messages) {
        // 标示一个Session的唯一性
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identifies.add(identify);
        }

        // 异步的数据库查询，并异步的发起二次通知
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];

                int index = 0;
                for (Session.Identify identify : identifies) {
                    Session session = SessionHelper.findFromLocal(identify.id);

                    if (session == null) {
                        // 第一次聊天，创建一个你和对方的一个会话
                        session = new Session(identify);
                    }

                    // 把会话，刷新到当前Message的最新状态
                    session.refreshToNow();
                    // 数据存储
                    adapter.save(session);
                    // 添加到集合
                    sessions[index++] = session;
                }

                // 调用直接进行一次通知分发
                instance.notifySave(Session.class, sessions);

            }
        }).build().execute();
    }

    /**
     * 通知监听器
     */
    @SuppressWarnings({"unused", "unchecked"})
    public interface ChangedListener<Data extends BaseModel> {
        void onDataSave(Data... list);

        void onDataDelete(Data... list);
    }
}
