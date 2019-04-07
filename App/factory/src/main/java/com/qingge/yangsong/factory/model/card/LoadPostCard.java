package com.qingge.yangsong.factory.model.card;


import java.util.List;

public class LoadPostCard {
    private List<PostCard> cards;
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
