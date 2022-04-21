package com.groupsix.project3_cst438.retrofit;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("userId")
    public Integer id;

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    @SerializedName("admin")
    public boolean admin;
}
