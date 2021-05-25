package com.mblj.mycontacts;

import java.io.Serializable;

public class Contact implements Serializable {
    private String id;
    private String firstname;
    private String lastname;
    private String phonenumber;
    private String imageUrl;
    private String imageName;

    public Contact() {}
    public Contact(String firstname, String lastname, String phonenumber, String imageUrl, String imageName) {
        this.setId(getId());
        this.setLastname(lastname);
        this.setFirstname(firstname);
        this.setPhonenumber(phonenumber);
        this.setImageUrl(imageUrl);
        this.setImageName(imageName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
