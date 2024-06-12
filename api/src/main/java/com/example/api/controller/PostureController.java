package com.example.api.controller;

import java.util.List;
import com.generated.api.PostureApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

import com.example.api.entities.PostureEntity;
import com.example.api.repositories.PostureRepository;
import com.example.api.services.PostureService;
import com.generated.model.Posture;
import com.generated.model.PostureUpdate;

@RestController
public class PostureController implements PostureApi {
    
    @Autowired
    private PostureRepository postureRepository;

    public PostureController(PostureRepository postureRepository) {
        this.postureRepository = postureRepository;
    }

    @Autowired
    PostureService postureService;

    @Override
    public ResponseEntity<List<Posture>> getPostures() {
        List<Posture> postures = postureService.getPostures();
        return ResponseEntity.ok(postures);
    }

    @Override
    public ResponseEntity<Posture> getPostureById(Long id) {
        Posture posture = postureService.getPostureById(id);
        if (posture == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<Posture> updatePostureById(Long id, PostureUpdate postureUpdate) {
        Posture posture = postureService.updatePostureById(id, postureUpdate);
        if (posture == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<List<Posture>> getPosturesByAnnotaterId(Long annotaterId) {
        List<Posture> postures = postureService.getPosturesByAnnotaterId(annotaterId);
        return ResponseEntity.ok(postures);
    }

    @Override
    public ResponseEntity<List<Posture>> getPosturesByUserId(Long userId) {
        List<Posture> postures = postureService.getPosturesByUserId(userId);
        return ResponseEntity.ok(postures);
    }

    @Override
    public ResponseEntity<Posture> getRandomPosture() {
        Posture posture = postureService.getRandomPosture();
        if (posture == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return ResponseEntity.ok(posture);
    }
}