package com.sarath.flipmap;

/**
 * Created by sarath on 1/7/2017.
 */

public class Contact {
    private String name;
    private String email;
    private String landLine;
    private String phoneNo;

    public Contact(String name,String email,String landLine,String phoneNo){
        this.name=name;
        this.email=email;
        this.landLine=landLine;
        this.phoneNo=phoneNo;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", landLine='" + landLine + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
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

    public String getLandLine() {
        return landLine;
    }

    public void setLandLine(String landLine) {
        this.landLine = landLine;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}

