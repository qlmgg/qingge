package com.qingge.post.bean.card;


import com.google.gson.annotations.Expose;

import java.util.List;

public class LoadPostCard {
    @Expose
    private List<PostCard> cards;
    @Expose
    private int pageCount;

    public List<PostCard> getCards() {
        return cards;
    }

    public void setCards(List<PostCard> cards) {
        this.cards = cards;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
