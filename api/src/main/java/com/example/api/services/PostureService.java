package com.example.api.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.repositories.PostureRepository;
import com.example.api.entities.PostureEntity;
import com.generated.model.Posture;
import com.generated.model.PostureUpdate;

@Service
public class PostureService {
    
    @Autowired
    private final PostureRepository postureRepository;

    public PostureService(PostureRepository postureRepository) {
        this.postureRepository = postureRepository;
    }

    @Transactional
    public Posture getPostureById(Long id) {
        PostureEntity posture = postureRepository.findById(id).orElse(null);
        if (posture == null) {
            return null;
        }

        return posture.toPosture();
    }

    @Transactional
    public List<Posture> getPosturesByAnnotaterId(Long annotaterId) {
        List<PostureEntity> postures = postureRepository.findByAnnotaterId(annotaterId);
        return postures.stream()
            .map(posture -> posture.toPosture())
            .collect(Collectors.toList());
    }

}
