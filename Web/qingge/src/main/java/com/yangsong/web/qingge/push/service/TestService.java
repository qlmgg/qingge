package com.yangsong.web.qingge.push.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestService {
    @GET
    public String test(){
        return "测试成功";
    }
}
