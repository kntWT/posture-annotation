package com.example.api.services;

import java.util.List;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.PessimisticLockingFailureException;

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
    public List<Posture> getPostures() {
        List<PostureEntity> postures = postureRepository.findAll();
        return PostureEntity.toPostures(postures);
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
        return PostureEntity.toPostures(postures);
    }

    @Transactional
    public List<Posture> getPosturesByUserId(Long userId) {
        List<PostureEntity> postures = postureRepository.findByUserId(userId);
        return PostureEntity.toPostures(postures);
    }

    @Transactional
    public Posture getRandomPosture() {
        List<PostureEntity> unannotatedPostures = postureRepository.findByAnnotaterIdIsNullLimitedTo(100);
        Collections.shuffle(unannotatedPostures);
        int index = 0;
        PostureEntity target = null;
        while (target == null && index < unannotatedPostures.size()) {
            try {
                target = postureRepository
                    .findByIdWithLock(unannotatedPostures.get(index).getId());
            } catch (PessimisticLockingFailureException e) {
                index++;
            }
        }

        if (target == null) {
            return null;
        }
        return target.toPosture();
    }

    @Transactional
    public Posture updatePostureById(Long id, PostureUpdate postureUpdate) {
        int updateCount = postureRepository
            .updatePostureById(
                id,
                postureUpdate.getNeckAngle(),
                postureUpdate.getTorsoAngle(),
                postureUpdate.getAnnotaterId()
            );
        if (updateCount <= 0) {
            return null;
        }
        
        return getPostureById(id);
    }

}
