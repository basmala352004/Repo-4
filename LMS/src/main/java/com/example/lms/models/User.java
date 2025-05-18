package com.example.lms.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "_users")
public class User implements UserDetails {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_INSTRUCTOR = "INSTRUCTOR";
    public static final String ROLE_STUDENT = "STUDENT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String password;
    private String role;
    private String email;

    // Constructor with role assignment
    public User(String name, String password, String role, String email) {
        this.name = name;
        this.password = password;
        this.role = assignCorrectRole(role); // Assign the role based on input
        this.email = email;
    }

    public User(Integer id, String email, String name) {
        this.id=id;
        this.email = email;
        this.name = name;
    }

    public User(Integer id, String name, String password, String role, String email) {
        this.id=id;
        this.name = name;
        this.password = password;
        this.role = assignCorrectRole(role); // Assign the role based on input
        this.email = email;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }


    // Method to assign the role
    private String assignCorrectRole(String role) {
        if (role == null || role.isEmpty()) {
            return ROLE_STUDENT; // Default role if null or empty
        }

        if (role.toLowerCase().startsWith("a")) {
            return ROLE_ADMIN;
        } else if (role.toLowerCase().startsWith("i")) {
            return ROLE_INSTRUCTOR;
        } else {
            return ROLE_STUDENT;
        }
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
