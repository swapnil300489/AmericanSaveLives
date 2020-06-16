package com.example.myapplication.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingCab {

    @SerializedName("message_code")
    @Expose
    private Integer messageCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Boolean details;

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

    public Boolean getDetails() {
        return details;
    }

    public void setDetails(Boolean details) {
        this.details = details;
    }

}
