package com.example.livia.iswint;

/**
 * Created by Livia on 6/7/2017.
 */

public class Organizer {

    private String type;
    private String name;
    private String email;
    private String phone;
    private String birthdate;
    private String workshop;
    private String room;
    private String gender;
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Organizer(String type, String name, String email, String phone, String birthdate, String workshop, String room, String gender, String country) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.workshop = workshop;
        this.room = room;
        this.gender = gender;
        this.type = type;
        this.country = country;
    }
}
