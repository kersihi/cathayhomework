package com.example.homework.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetail  implements Serializable {

    @SerializedName("login")
    public String login;

    @SerializedName("id")
    public String id;

    @SerializedName("avatar_url")
    public String avatar_url;

    @SerializedName("email")
    public String email;

    @SerializedName("location")
    public String location;

    @SerializedName("blog")
    public String blog;

    @SerializedName("name")
    public String name;

    @SerializedName("site_admin")
    public Boolean site_admin;

    public void setSite_admin(Boolean site_admin) {
        this.site_admin = site_admin;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public Boolean getSite_admin() {
        return site_admin;
    }

    public String getBlog() {
        return blog;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

}
