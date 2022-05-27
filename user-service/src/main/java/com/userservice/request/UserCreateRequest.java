package com.userservice.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.userservice.entity.User;

public class UserCreateRequest {
    
    @NotBlank
    private String name;

    private String contact;

    @Email
    @NotBlank
    private String email;

    public UserCreateRequest() {
    }

    public UserCreateRequest(@NotBlank String name, String contact, @Email @NotBlank String email) {
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User toUser() {
        
        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setContact(contact);

        return user;
    }
}
