package com.example.livia.iswint;

/**
 * Created by Livia on 6/3/2017.
 */

public class Blog {

    private String desc;
    private String image;
    private String uid;
    private String username;
    private  String profileimage;

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public Blog(){

    }

    public Blog(String desc, String image, String uid, String username, String profileimage ) {
        this.desc = desc;
        this.image = image;
        this.uid = uid;
        this.username = username;
        this.profileimage = profileimage;
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

