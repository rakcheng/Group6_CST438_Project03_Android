package com.groupsix.project3_cst438.retrofit;


import com.google.gson.annotations.SerializedName;
import com.groupsix.project3_cst438.roomDB.entities.User;

public class UserResponse {
    @SerializedName("userId")
    private Integer userId;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("isAdmin")
    private boolean isAdmin;

    public User getUser() {
        return new User(userId, username, password, isAdmin);
    }
}
