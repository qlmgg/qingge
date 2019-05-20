package com.qingge.yangsong.qingge;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.igexin.sdk.PushManager;
import com.qingge.yangsong.common.app.Application;
import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.qingge.getuiService.DemoIntentService;
import com.qingge.yangsong.qingge.getuiService.DemoPushService;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 调用Factory进行初始化
        Factory.setup();
        // 推送进行初始化
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(),DemoPushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),DemoIntentService.class);


        Phoenix.config()
                .imageLoader((mContext, imageView, imagePath, type) -> Glide.with(mContext)
                        .load(imagePath)
                        .into(imageView));
    }
}
