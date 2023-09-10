package com.api.login.dto;

import com.api.login.models.User;

public record UserListResponse(
        String email, String name, String profileImage) {
    public UserListResponse(User user){
        this(user.getEmail(), user.getName(), user.getProfileImage());
    }

}
