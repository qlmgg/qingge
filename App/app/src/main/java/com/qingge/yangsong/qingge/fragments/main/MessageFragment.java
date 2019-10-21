package com.qingge.yangsong.qingge.fragments.main;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingge.yangsong.common.app.PresenterFragment;
import com.qingge.yangsong.common.widget.EmptyView;
import com.qingge.yangsong.common.widget.PortraitView;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.face.Face;
import com.qingge.yangsong.factory.model.db.Session;
import com.qingge.yangsong.factory.presenter.message.SessionContract;
import com.qingge.yangsong.factory.presenter.message.SessionPresenter;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.qingge.activity.ChatGroupActivity;
import com.qingge.yangsong.qingge.activity.ChatUserActivity;
import com.qingge.yangsong.utils.DateTimeUtil;

import butterknife.BindView;

import static com.qingge.yangsong.factory.model.db.Message.RECEIVER_TYPE_GROUP;

public class MessageFragment extends PresenterFragment<SessionContract.Presenter>
        implements SessionContract.View{
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    private RecyclerAdapter<Session> mAdapter;
    @BindView(R.id.layout)
    SwipeRefreshLayout mLayout;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mLayout.setColorSchemeColors(getResources().getColor(R.color.black));
        /*
         * 设置下拉刷新的监听
         */
        mLayout.setOnRefreshListener(() -> {
            //TODO 刷新
            mLayout.setRefreshing(false);
        });


        // 初始化Recycler
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                // 返回cell的布局id
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new MessageFragment.ViewHolder(root);
            }
        });


        // 点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListener<Session>() {

            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                // 跳转到聊天界面
                if (session.getReceiverType() == RECEIVER_TYPE_GROUP)
                    ChatGroupActivity.show(getContext(), session);
                else
                    ChatUserActivity.show(getContext(), session);
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Session session) {

            }
        });

        // 初始化占位布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }


    // 界面数据渲染
    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.txt_time)
        TextView mTime;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {

            mPortraitView.setup(Glide.with(getContext()), session.getPicture());
            mName.setText(session.getTitle());
            String str = TextUtils.isEmpty(session.getContent()) ? "" : session.getContent();
            Spannable spannable = new SpannableString(str);
            // 解析表情
            Face.decode(mContent, spannable, (int) mContent.getTextSize());
            // 把内容设置到布局上
            mContent.setText(spannable);

            mTime.setText(DateTimeUtil.getSampleDate(session.getModifyAt()));
        }
    }
}
