package com.example.api.services;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.PessimisticLockingFailureException;

import com.example.api.repositories.PostureRepository;
import com.example.api.utils.DateFormatter;
import com.example.api.utils.SaveFile;
import com.example.api.entities.PostureEntity;
import com.generated.model.Posture;
import com.generated.model.PostureUpdate;
import com.generated.model.PostureUpdateWithFile;
import com.generated.model.PostureUpdateMarkerPosition;

@Service
public class PostureService {

    @Autowired
    private static PostureRepository postureRepository;

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
    public List<Posture> getPosturesByIsSample(Boolean isSample) {
        List<PostureEntity> postures = postureRepository.findByIsSample(isSample);
        return PostureEntity.toPostures(postures);
    }

    @Transactional
    public List<Posture> getPosturesByUserId(Long userId) {
        List<PostureEntity> postures = postureRepository.findByUserId(userId);
        return PostureEntity.toPostures(postures);
    }

    @Transactional
    public Posture getRandomPosture() {
        List<PostureEntity> unannotatedPostures = postureRepository.findOrderByAnnotationCountLimitedTo(100);
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
    public Posture getRandomPostureByAnnotaterId(Long annotaterId) {
        List<PostureEntity> unannotatedPostures = postureRepository.findByAnnotaterIdOrderByAnnotationCountLimitTo(annotaterId, 100, 5);
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
                postureUpdate.getTorsoAngle()
            );
        if (updateCount <= 0) {
            return null;
        }

        return getPostureById(id);
    }

    @Transactional
    public Posture updatePostureByIdAndSaveFile(Long id, PostureUpdateWithFile postureUpdateWithFile) {
        PostureUpdate postureUpdate = new PostureUpdate(
            postureUpdateWithFile.getNeckAngle(),
            postureUpdateWithFile.getTorsoAngle(),
            postureUpdateWithFile.getAnnotaterId()
        );
        Posture posture = updatePostureById(id, postureUpdate);
        if (posture == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss.SSS");
        String fileName = formatter.format(posture.getExCreatedAt()) + ".jpg";
        String basePath = System.getenv("IMAGE_DIR");
        String dir = Paths.get(
            basePath,
            posture.getUserId().toString(),
            posture.getAnnotaterId().toString()
        ).toString();
        boolean success = SaveFile.saveBase64Image(fileName, postureUpdateWithFile.getFile(), dir);
        if (!success) {
            return null;
        }

        return posture;
    }

    @Transactional
    public Posture updatePostureMarkerById(Long id, PostureUpdateMarkerPosition postureUpdate) {
        int updateCount = postureRepository
            .updatePostureById(
                id,
                postureUpdate.getTragusX().get(),
                postureUpdate.getTragusY().get(),
                postureUpdate.getShoulderX().get(),
                postureUpdate.getShoulderY().get(),
                postureUpdate.getWaistX().get(),
                postureUpdate.getWaistY().get(),
                postureUpdate.getImageWidth().get(),
                postureUpdate.getImageHeight().get()
            );
        if (updateCount <= 0) {
            return null;
        }
        return getPostureById(id);
    }

    @Transactional
    public Long updatePostureMarkers(List<PostureUpdateMarkerPosition> postureUpdates) {
        Long updatedCount = 0L;
        for (PostureUpdateMarkerPosition postureUpdate : postureUpdates) {
            int count = postureRepository
                .updatePostureById(
                    postureUpdate.getId().get(),
                    postureUpdate.getTragusX().get(),
                    postureUpdate.getTragusY().get(),
                    postureUpdate.getShoulderX().get(),
                    postureUpdate.getShoulderY().get(),
                    postureUpdate.getWaistX().get(),
                    postureUpdate.getWaistY().get(),
                    postureUpdate.getImageWidth().get(),
                    postureUpdate.getImageHeight().get()
                );
            if (count > 0) {
                updatedCount++;
            }
        }
        return updatedCount;
    }

    @Transactional
    public static Long lastPostureId() {
        return postureRepository.findLastId();
    }

    @Transactional
    public static List<Posture> createSamplePostures(Long count) {
        List<PostureEntity> postures = postureRepository.findByOrderByRandomLimitTo(count);
        Long lastId = lastPostureId();
        System.out.println("lastId: " + lastId);
        List<PostureEntity> samplePostures = new ArrayList<PostureEntity>();
        for (PostureEntity posture : postures) {
            samplePostures.add(posture
                .cloneWithoutId()
                .setId(++lastId)
                .setIsSample(true)
            );
        }
        List<PostureEntity> savedPostures = postureRepository.saveAll(samplePostures);
        return PostureEntity.toPostures(savedPostures);
    }

    @Transactional
    public Posture getRandomSamplePosture() {
        List<PostureEntity> unannotatedPostures = postureRepository.findSampleByOrderByAnnotationCountLimitedTo(100);
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
    public Posture getRandomSamplePostureByAnnotaterId(Long annotaterId) {
        List<PostureEntity> unannotatedPostures = postureRepository.findSampleByAnnotaterIdOrderByAnnotationCountLimitTo(annotaterId, 100, 5);
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

}
