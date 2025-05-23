package com.example.api.controller;

import java.util.List;
import com.generated.api.PostureApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import com.generated.model.PostureUpdateWithFile;
import com.generated.model.PostureUpdateMarkerPosition;
import com.generated.model.PostureWithAnnotations;
import com.generated.model.PostureWithPageInfo;

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
    public ResponseEntity<PostureWithPageInfo> getPostures(Pageable pageable) {
        PostureWithPageInfo postures = postureService.getPostures(pageable);
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
    public ResponseEntity<Posture> updatePostureById(Long id, PostureUpdateWithFile postureUpdate) {
        // Posture posture = postureService.updatePostureById(id, postureUpdate);
        Posture posture = postureService.updatePostureByIdAndSaveFile(id, postureUpdate);
        if (posture == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<PostureWithPageInfo> getPosturesByUserId(Long userId, Pageable pageable) {
        PostureWithPageInfo postures = postureService.getPosturesByUserId(userId, pageable);
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

    @Override
    public ResponseEntity<Posture> getRandomPostureByAnnotaterId(Long annotaterId) {
        Posture posture = postureService.getRandomPostureByAnnotaterId(annotaterId);
        if (posture == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<Posture> getRandomPostureThinOutById(Long step) {
        Posture posture = postureService.getRandomPostureThinOutById(step);
        if (posture == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<Posture> getRandomPostureByAnnotaterIdThinOutById(Long annotaterId,
            Long step) {
        Posture posture =
                postureService.getRandomPostureByAnnotaterIdThinOutById(annotaterId, step);
        if (posture == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<Posture> updatePostureMarkerById(Long id,
            PostureUpdateMarkerPosition postureUpdateMarkerPosition) {
        Posture posture = postureService.updatePostureMarkerById(id, postureUpdateMarkerPosition);
        if (posture == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<Long> updatePostureMarkers(
            List<PostureUpdateMarkerPosition> markerPositions) {
        Long updatedCount = postureService.updatePostureMarkers(markerPositions);
        return ResponseEntity.ok(updatedCount);
    }

    @Override
    public ResponseEntity<PostureWithPageInfo> getSamplePostures(Pageable pageable) {
        PostureWithPageInfo postures = postureService.getPosturesByIsSample(true, pageable);
        return ResponseEntity.ok(postures);
    }

    @Override
    public ResponseEntity<Posture> getRandomSamplePosture() {
        Posture posture = postureService.getRandomSamplePosture();
        if (posture == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<Posture> getRandomSamplePostureByAnnotaterId(Long annotaterId) {
        Posture posture = postureService.getRandomSamplePostureByAnnotaterId(annotaterId);
        if (posture == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(posture);
    }

    @Override
    public ResponseEntity<PostureWithAnnotations> getPostureWithAnnotationsById(Long id) {
        PostureWithAnnotations postureWithAnnotations =
                postureService.getPostureWithAnnotationsById(id);
        if (postureWithAnnotations == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(postureWithAnnotations);
    }
}
