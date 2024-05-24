package com.example.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.repositories.UserRepository;
import com.example.api.entities.UserEntity;
import com.generated.model.User;
import com.generated.model.UserCreate;

@Service
public class UserService {
   
    @Autowired 
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(UserCreate userCreate) {
        String name = userCreate.getName();
        String password = userCreate.getPassword();
        UserEntity existUser = userRepository.findByNameAndPassword(name, password);
        if (existUser == null) {
            UserEntity user = new UserEntity(name, password);
            return userRepository.save(user).toUser();
        } else {
            return null;
        }
    }

    @Transactional
    public User loginUser(UserCreate userCreate) {
        String name = userCreate.getName();
        String password = userCreate.getPassword();
        UserEntity user = userRepository.findByNameAndPassword(name, password);
        if (user != null) {
            return user.toUser();
        }

        return null;
    }
}
