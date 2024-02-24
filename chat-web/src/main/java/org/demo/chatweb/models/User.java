package org.demo.chatweb.models;



import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;

@Entity
@Table(name = "Person")

public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    @NotNull
    private String username;
    @Column(name = "email")
    @Email
    private String email;
    @Column(name = "date_of_birth")
    @NotNull
    private Date dateOfBirth;
    @Column(name = "phone")
    @NotBlank
    @Pattern(regexp = "^\\+\\d{11}$", message = "Phone number must start with + and be followed by 11 digits")
    private String phone;
    @Column(name = "password")
    @NotNull
    @Size(min = 4)
    private String password;
    @Column(name = "role")
    @NotNull
    @Pattern(regexp = "^(ROLE_USER|ROLE_ADMIN)$")
    private String role;

    @Column(name = "hide")
    @NotNull
    private Boolean hideProfile;


    @Column(name = "avatar")
    private String avatar;

    @Column(name = "is_online")
    private Boolean isOnline;
    public User(){}

    public User(String username, String email, Date dateOfBirth, String phone, String password) {
        this.username = username;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getHideProfile() {
        return hideProfile;
    }

    public void setHideProfile(Boolean hideProfile) {
        this.hideProfile = hideProfile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", hideProfile=" + hideProfile +
                ", avatar='" + avatar + '\'' +
                ", isOnline=" + isOnline +
                '}';
    }
}
