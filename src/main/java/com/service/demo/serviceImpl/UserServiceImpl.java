package com.service.demo.serviceImpl;

import com.service.demo.GlobalException.ResourceNotFoundException;
import com.service.demo.dto.EmailDto;
import com.service.demo.dto.LoginDto;
import com.service.demo.emailService.EmailService;
import com.service.demo.model.User;
import com.service.demo.repo.UserRepo;
import com.service.demo.service.UserService;
import com.service.demo.tokenHandling.JwtUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private JwtUtill jwtUtill;
    private RedisTemplate redisTemplate;
    private RedisService redisService;
    private EmailService emailService;


    @Autowired
    public UserServiceImpl(UserRepo userRepo, JwtUtill jwtUtill, RedisTemplate redisTemplate, RedisService redisService, EmailService emailService) {
        this.userRepo = userRepo;
        this.jwtUtill = jwtUtill;
        this.redisTemplate = redisTemplate;
        this.redisService = redisService;
        this.emailService = emailService;
    }


    @Override
    public User add(User user) {
        String password = UUID.randomUUID().toString();
        user.setPassword(password);
        return  userRepo.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found with id: "+ id));
    }

    // redis cache used to fast accessing.......
    @Override
    public User getUser(String name) {
        User user = redisService.get(name, User.class);
        if(user!=null){
            return user;
        }else{
            Optional<User> byName = userRepo.findByName(name);
            User user1 = byName.get();
            if(user1!=null){

                redisService.set(name,user1,300L);
            }
               return user1;
        }
    }

    @Override
    public String verifyLogin(LoginDto loginDto) {
        Optional<User> user = userRepo.findByName(loginDto.getName());
        if(user.isPresent()){
         User user1 =   user.get();
            if(user1.getPassword().equals(loginDto.getPassword())){
               return jwtUtill.generateToken(user1);
            }
            throw new ResourceNotFoundException("invalid password!!");
        }

        throw new ResourceNotFoundException(" not found exception");

    }

    @Override
    public void forgotPassword(String email) {
           if(userRepo.existsByEmail(email)){
               String token = UUID.randomUUID().toString();
               EmailDto emailDto = new EmailDto();
               emailDto.setEmail(email);
               emailDto.setToken(token);
               emailService.sendSimpleEmail("balorclub7047@gmail.com","Reset password token",emailDto.toString());
               redisService.set("emailToken",emailDto,300L);

           }
    }

    @Override
    public String resetPassword(EmailDto emailDto, String newPassword) {
        String token = emailDto.getToken();
        EmailDto emailToken = redisService.get("emailToken", EmailDto.class);
         if(token.equals(emailToken.getToken()) && emailDto.getEmail().equals(emailToken.getEmail())){

             String email = emailToken.getEmail();
             User user = userRepo.findByEmail(email);
             user.setPassword(newPassword);
             userRepo.save(user);

         }else{
             return "invalid email && unauthorized user";
         }
         return "";
    }


}
