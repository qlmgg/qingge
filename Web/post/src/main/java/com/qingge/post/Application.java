package com.qingge.post;

import com.qingge.post.provider.AuthRequestFilter;
import com.qingge.post.provider.GsonProvider;
import com.qingge.post.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

public class Application extends ResourceConfig {
    public Application(){
        //注册逻辑处理的包名
        packages(AccountService.class.getPackage().getName());
        //注册Gson解析器
        register(GsonProvider.class);
        //注册日志打印
        register(Logger.class);
        // 注册我们的全局请求拦截器
        register(AuthRequestFilter.class);
    }
}
