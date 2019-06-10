package com.qingge.post.bean.api.university;

import com.google.gson.annotations.Expose;
import com.qingge.post.bean.card.GroupCard;
import com.qingge.post.bean.card.PostCard;

import java.util.List;

//学校范围切换返回的
public class SchoolInfoModel {
    @Expose
    private int pageCount;
    @Expose
    private List<PostCard> postCards;
    @Expose
    private List<GroupCard> groupCards;

    public SchoolInfoModel(int pageCount, List<PostCard> postCards, List<GroupCard> groupCards) {
        this.pageCount = pageCount;
        this.postCards = postCards;
        this.groupCards = groupCards;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<PostCard> getPostCards() {
        return postCards;
    }

    public void setPostCards(List<PostCard> postCards) {
        this.postCards = postCards;
    }

    public List<GroupCard> getGroupCards() {
        return groupCards;
    }

    public void setGroupCards(List<GroupCard> groupCards) {
        this.groupCards = groupCards;
    }
}
