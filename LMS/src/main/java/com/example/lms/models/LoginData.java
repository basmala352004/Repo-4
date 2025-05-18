package com.example.lms.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class LoginData {

    public LoginData() {}
    public LoginData(String username, String password, String email ) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String  getUsername() {
        return  username;
    }

    private String username;
    private String password;
    private String  email;

    public void setPassword(String password) {
        this.password = password;
    }
}