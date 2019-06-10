package com.qingge.yangsong.qingge.activity;

import android.content.Context;
import android.content.Intent;

import com.qingge.yangsong.common.app.Activity;
import com.qingge.yangsong.qingge.R;

public class SendPostActivity extends Activity {

//    @BindView(R.id.tv_back)
//    ImageView mBack;

    public static void show(Context context){
        Intent intent = new Intent(context,SendPostActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_send_post;
    }


//    @OnClick(R.id.tv_back)
//    public void back(){
//        finish();
//    }
}
