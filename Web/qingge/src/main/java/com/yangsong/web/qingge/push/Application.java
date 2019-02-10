package com.yangsong.web.qingge.push;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.yangsong.web.qingge.push.provider.GsonProvider;
import com.yangsong.web.qingge.push.service.AccountService;
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
    }
}
