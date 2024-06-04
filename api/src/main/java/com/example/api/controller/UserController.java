package com.example.api.controller;

import com.generated.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

import com.example.api.entities.UserEntity;
import com.example.api.repositories.UserRepository;
import com.example.api.services.UserService;
import com.generated.model.User;
import com.generated.model.UserCreate;

@RestController
public class UserController implements UserApi {
    
    private UserRepository userRepository;
    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<User>  createUser(UserCreate userCreate) {
        UserEntity exitUser = userRepository.findByNameAndPassword(userCreate.getName(), userCreate.getPassword());
        if (exitUser != null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.createUser(userCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Override
    public ResponseEntity<User> loginUser(UserCreate userCreate) {
        User user = userService.loginUser(userCreate);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<User> getUserByToken(String authorization) {
        if (!authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorization.substring(7);
        User user = userService.getUserByToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(user);
    }
}
