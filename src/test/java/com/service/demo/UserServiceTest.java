package com.service.demo;

import com.service.demo.model.User;
import com.service.demo.repo.UserRepo;
import com.service.demo.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    private RedisTemplate redisTemplate;

    @Test
    void testGetUserAll(){

       User user = new User();
       user.setName("john");
       user.setPassword("root");
       user.setEmail("john@gmail.com");
       user.setRole("admin");
       user.setId(12l);

        User user1 = new User();
        user1.setName("alex");
        user1.setPassword("root123");
        user1.setEmail("alex@gmail.com");
        user1.setRole("user");
        user1.setId(13l);

       List<User> list = new ArrayList<>();
       list.add(user);
       list.add(user1);


  when(userRepo.findAll()).thenReturn(list);

        List<User> all = userService.getAll();
        assertEquals("john", all.get(0).getName());
        assertEquals("alex", all.get(1).getName());
    }


    @Test
    void testAddUser(){

      User inputUser =  new User();
      inputUser.setName("john");
      inputUser.setEmail("john@gmail.com");
      inputUser.setRole("user");

         User savedUser = new User();
         savedUser.setId(1L);
         savedUser.setName("john");
         savedUser.setEmail("john@gmail.com");

         when(userRepo.save(inputUser)).thenReturn(savedUser);
        User add = userService.add(inputUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(captor.capture());
        User capturedUser = captor.getValue();

        assertNotNull(capturedUser.getPassword());
        assertNotNull(capturedUser.getPassword().isBlank());
        assertEquals(add,savedUser);
    }




}
