package com.example.myapplication.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;





public class Login {

    @SerializedName("message_code")
    @Expose
    private Integer messageCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

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

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }


    public class Detail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("mobile_no")
        @Expose
        private String mobileNo;
        @SerializedName("profile_img")
        @Expose
        private Object profileImg;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("verified")
        @Expose
        private String verified;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("last_login")
        @Expose
        private String lastLogin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public Object getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(Object profileImg) {
            this.profileImg = profileImg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVerified() {
            return verified;
        }

        public void setVerified(String verified) {
            this.verified = verified;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

    }
}
