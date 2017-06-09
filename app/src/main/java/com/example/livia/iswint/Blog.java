package com.example.livia.iswint;

/**
 * Created by Livia on 6/3/2017.
 */

public class Blog {

    private String desc;
    private String image;
    private String uid;
    private String username;

    public Blog(){

    }

    public Blog(String desc, String image, String uid, String username) {
        this.desc = desc;
        this.image = image;
        this.uid = uid;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

