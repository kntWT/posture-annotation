package com.example.api.repositories;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.entities.PostureEntity;

public interface PostureRepository extends JpaRepository<PostureEntity, Long>{

    public PostureEntity findByInId(Long inId);

    public List<PostureEntity> findByAnnotaterId(Long annotaterId);

    public List<PostureEntity> findByUserId(Long userId);
    
}
