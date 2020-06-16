package com.example.myapplication.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class DriverDetails {

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
        @SerializedName("mobile_no")
        @Expose
        private String mobileNo;
        @SerializedName("profile_img")
        @Expose
        private String profileImg;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("latitude_")
        @Expose
        private String latitude;
        @SerializedName("longitude_")
        @Expose
        private String longitude;
        @SerializedName("ac_type")
        @Expose
        private String acType;
        @SerializedName("sheet")
        @Expose
        private String sheet;
        @SerializedName("wheels")
        @Expose
        private String wheels;
        @SerializedName("per_hr")
        @Expose
        private String perHr;
        @SerializedName("old_year")
        @Expose
        private String oldYear;
        @SerializedName("dname")
        @Expose
        private String dname;
        @SerializedName("license")
        @Expose
        private String license;
        @SerializedName("review")
        @Expose
        private String review;

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

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

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
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

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getAcType() {
            return acType;
        }

        public void setAcType(String acType) {
            this.acType = acType;
        }

        public String getSheet() {
            return sheet;
        }

        public void setSheet(String sheet) {
            this.sheet = sheet;
        }

        public String getWheels() {
            return wheels;
        }

        public void setWheels(String wheels) {
            this.wheels = wheels;
        }

        public String getPerHr() {
            return perHr;
        }

        public void setPerHr(String perHr) {
            this.perHr = perHr;
        }

        public String getOldYear() {
            return oldYear;
        }

        public void setOldYear(String oldYear) {
            this.oldYear = oldYear;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

    }

}

