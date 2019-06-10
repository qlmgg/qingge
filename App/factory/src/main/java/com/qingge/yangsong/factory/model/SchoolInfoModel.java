package com.qingge.yangsong.factory.model;

import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.card.PostCard;


import java.util.List;

//学校范围切换返回的
public class SchoolInfoModel {

    private int pageCount;
    private List<PostCard> postCards;
    private List<GroupCard>groupCards;



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

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
