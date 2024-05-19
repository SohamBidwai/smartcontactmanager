package com.smartcontactmanager.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "CONTACT")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cid;
    private String name;
    private String second_name;
    private String work;
    @Column(unique = true)
    private String email;
    private String mobile;
    private String image;
    @Column(length = 500)
    private String description;

    @ManyToOne
    private User user;

    public Contact() {
        super();
    }

    public Contact(int cid, String name, String second_name, String work, String email, String mobile, String image, String description) {
        this.cid = cid;
        this.name = name;
        this.second_name = second_name;
        this.work = work;
        this.email = email;
        this.mobile = mobile;
        this.image = image;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", second_name='" + second_name + '\'' +
                ", work='" + work + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}