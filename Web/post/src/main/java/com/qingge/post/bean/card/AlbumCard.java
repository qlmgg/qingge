package com.qingge.post.bean.card;

import com.google.gson.annotations.Expose;

public class AlbumCard {

    @Expose
    private String address;

    public AlbumCard(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
