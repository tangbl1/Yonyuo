package com.yonyou.iuap.corp.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserIdResponse {

    /**
     * 获取的友互通id
     */
    @JsonProperty(value = "user_id")
    private String user_id;

    @JsonProperty(value = "name")
    private String name;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
