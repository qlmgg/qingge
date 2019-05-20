package com.qingge.yangsong.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * 数据库的基本信息
 *
 * @version 1.0.0
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 7;

    public static void delete(){
        SQLite.delete(Group.class).execute();
        SQLite.delete(GroupMember.class).execute();
        SQLite.delete(Message.class).execute();
        SQLite.delete(Post.class).execute();
        SQLite.delete(Session.class).execute();
        SQLite.delete(University.class).execute();
        SQLite.delete(User.class).execute();
    }
}
