package com.example.lms.models;

import jakarta.persistence.*;

@Entity
@Table(name = "_profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer profileId;  // ✅ Use dedicated primary key instead of user_id
    private String address;
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id", unique = true)  // ✅ Correct foreign key mapping
    private User user;

    public Profile() {
        this.address = "";
        this.phoneNumber = "";
    }

    public Profile(User user) {
        this.address = "";
        this.phoneNumber = "";
        this.user = user;
    }


    public Integer getUserId() {
        return user.getId();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return profileId;
    }

    public void setId(Integer profileId) {
        this.profileId = profileId;
    }
}