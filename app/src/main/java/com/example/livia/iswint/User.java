package com.example.livia.iswint;

/**
 * Created by Livia on 6/17/2017.
 */

public class User {

    private String user_name;
    private String user_id;
    private String user_image;

    public User(){

    }

    public User(String user_name, String user_id, String user_image) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
