package com.example.myapplication.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TokenSave {

    @SerializedName("message_code")
    @Expose
    private Integer messageCode;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(Integer messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
