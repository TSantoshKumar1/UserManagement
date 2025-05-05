package com.service.demo.controller;

import com.service.demo.dto.EmailDto;
import com.service.demo.dto.LoginDto;
import com.service.demo.jwtBlacklistService.JwtBlackListService;
import com.service.demo.model.User;
import com.service.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/demoapi")
public class UserController {

    private UserService userService;
    private JwtBlackListService jwtBlackListService;

    @Autowired
    public UserController(UserService userService, JwtBlackListService jwtBlackListService) {
        this.userService = userService;
        this.jwtBlackListService = jwtBlackListService;
    }

     private Logger logger= LoggerFactory.getLogger(UserController.class);


    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user){
        User savedUser = userService.add(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<List<User>>getAll(){
        List<User> allUser = userService.getAll();
        return new ResponseEntity<>(allUser,HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable  Long id){
        User user = userService.getUser(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<User> getUser(@PathVariable  String name){
        User user = userService.getUser(name);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String token = userService.verifyLogin(loginDto);
        logger.info("jwt token {}",token);
        return new ResponseEntity<>("Bearer "+token,HttpStatus.OK);

    }

@PostMapping("/logout")
public ResponseEntity<String>logout(@RequestHeader("Authorization") String authorizationHeader){

    String token = authorizationHeader.replace("Bearer ", "");
       jwtBlackListService.blacklistToken(token);
       return new ResponseEntity<>("logged out successfully",HttpStatus.OK);
}

@PostMapping("/forgot/{email}")
public ResponseEntity<String>forgot(@PathVariable String email){
        userService.forgotPassword(email);
        return new ResponseEntity<>("token send to email for reset password!!",HttpStatus.OK);
}


@PostMapping("/reset/{newPassword}")
public ResponseEntity<String>resetPassword(@RequestBody EmailDto emailDto, @PathVariable String newPassword){
       userService.resetPassword(emailDto, newPassword);
       return new ResponseEntity<>("password reset successfully!!",HttpStatus.OK);
}


    @GetMapping("/profile")
    public ResponseEntity<User> profile(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(user ,HttpStatus.OK);
    }



    @GetMapping("/demo")
    public String demo(){

        return "accessable";
    }

}
