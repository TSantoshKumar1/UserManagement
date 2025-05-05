package com.service.demo.service;


import com.service.demo.dto.EmailDto;
import com.service.demo.dto.LoginDto;
import com.service.demo.model.User;

import java.util.List;

public interface UserService {

    public User add(User user);
    public List<User> getAll();
    public User getUser(Long id);
    public User getUser(String name);
    public String verifyLogin(LoginDto loginDto);
    public void forgotPassword(String email);
    public String resetPassword(EmailDto emailDto,String newPassword);
}
