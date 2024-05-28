package com.example.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

        public UserEntity findByNameAndPassword(String name, String password);

        public UserEntity findByToken(String token);

}
