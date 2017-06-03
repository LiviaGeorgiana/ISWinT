package com.example.livia.iswint;

/**
 * Created by Livia on 6/3/2017.
 */

public class Blog {

    private String desc;
    private String image;
    private String uid;

    public Blog(){

    }

    public Blog(String desc, String image, String uid) {
        this.desc = desc;
        this.image = image;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}

