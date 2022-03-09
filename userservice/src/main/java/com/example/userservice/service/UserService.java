package com.example.userservice.service;

import com.example.userservice.domain.Role;
import com.example.userservice.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName); // Never duplicate username! UNIQUE & NOT NULL
    User getUser(String username);
    List<User> getUsers(); // 페이징 처리 필요
}
