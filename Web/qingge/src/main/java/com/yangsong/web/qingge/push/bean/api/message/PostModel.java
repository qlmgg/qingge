package com.yangsong.web.qingge.push.bean.api.message;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class PostModel {
    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private String attach;
    @Expose
    private String senderId;
    @Expose
    private String UniversityId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getUniversityId() {
        return UniversityId;
    }

    public void setUniversityId(String universityId) {
        UniversityId = universityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // 校验
    public static boolean check(PostModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.content)
                && !Strings.isNullOrEmpty(model.senderId)
                && !Strings.isNullOrEmpty(model.UniversityId);
    }
}
