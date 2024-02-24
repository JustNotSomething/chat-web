package org.demo.chatweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserMinDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;
    public UserMinDTO(){}

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

    @Override
    public String toString() {
        return "UserMinDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMinDTO that = (UserMinDTO) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
