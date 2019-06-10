package com.qingge.yangsong.qingge.fragments.panel;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.common.widget.recycler.RecyclerAdapter;
import com.qingge.yangsong.face.Face;
import com.qingge.yangsong.qingge.R;
import com.qingge.yangsong.utils.UiTool;

import net.qiujuer.genius.ui.Ui;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanelFragment extends com.qingge.yangsong.common.app.Fragment {
    private View mFacePanel, mGalleryPanel, mRecordPanel;
    private PanelCallback mCallback;

    public PanelFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initFace(root);
        initRecord(root);
        initGallery(root);
    }

    //初始化
    public void setup(PanelCallback callback) {
        mCallback = callback;
    }

    private void initFace(View root) {
        final View facePanel = mFacePanel = root.findViewById(R.id.lay_panel_face);

        View backspace = facePanel.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(v -> {
            // 删除逻辑
            PanelCallback callback = mCallback;
            if (callback == null)
                return;

            // 模拟一个键盘删除点击
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL,
                    0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            callback.getInputEditText().dispatchKeyEvent(event);
        });

        TabLayout tabLayout = facePanel.findViewById(R.id.tab);
        ViewPager viewPager = facePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);

        // 每一表情显示48dp
        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        final int totalScreen = UiTool.getScreenWidth(getActivity());
        final int spanCount = totalScreen / minFaceSize;

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                //添加
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;
                recyclerView.setAdapter(new FaceAdapter(faces, new RecyclerAdapter.AdapterListener<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {
                        if (mCallback == null)
                            return;
                        //表情添加到输入框
                        EditText editText = mCallback.getInputEditText();
                        Face.inputFace(getContext(), editText.getText(), bean, (int) editText.getTextSize());

                    }

                    @Override
                    public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {

                    }
                }));

                //添加到容器
                container.addView(recyclerView);

                return recyclerView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                //移除
                container.removeView((View) object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                //拿到表情盘的描述
                return Face.all(getContext()).get(position).name;
            }
        });
    }

    private void initRecord(View root) {

    }

    private void initGallery(View root) {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_panel;
    }


    public void showFace() {
//        mRecordPanel.setVisibility(View.GONE);
//        mGalleryPanel.setVisibility(View.GONE);
//        mFacePanel.setVisibility(View.VISIBLE);
    }

    public void showGallery() {
        Application.showToast("图片");

    }

    public void showRecord() {
        Application.showToast("语音");

    }

    // 回调聊天界面的Callback
    public interface PanelCallback {
        EditText getInputEditText();

    }
}
