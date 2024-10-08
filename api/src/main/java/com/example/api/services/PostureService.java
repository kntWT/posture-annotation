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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.api.repositories.PostureRepository;
import com.example.api.utils.DateFormatter;
import com.example.api.utils.SaveFile;
import com.example.api.entities.PostureEntity;
import com.generated.model.Posture;
import com.generated.model.PostureUpdate;
import com.generated.model.PostureUpdateWithFile;
import com.generated.model.PostureUpdateMarkerPosition;
import com.generated.model.PostureWithAnnotations;
import com.generated.model.PostureWithPageInfo;

@Service
public class PostureService {

    // n件の中からランダムで取得
    private final int RANDOM_POOL = 50;

    // n件以上アノテーションが集まってほしいか
    private final int MAX_NEED_COUNT = Integer.parseInt(System.getenv("GOAL_ANNOTATION_COUNT_PER_POSTURE"));

    @Autowired
    private static PostureRepository postureRepository;

    public PostureService(PostureRepository postureRepository) {
        this.postureRepository = postureRepository;
    }

    @Transactional
    public PostureWithPageInfo getPostures(Pageable pageable) {
        Page<PostureEntity> entity = postureRepository.findAll(pageable);
        PostureWithPageInfo postures = PostureEntity.toPostureWithPageInfo(entity);
        return postures;
    }

    @Transactional
    public Posture getPostureById(Long id) {
        PostureEntity posture = postureRepository.findById(id).orElse(null);
        if (posture == null) {
            return null;
        }

        return PostureEntity.toPosture(posture);
    }

    @Transactional
    public PostureWithPageInfo getPosturesByIsSample(Boolean isSample, Pageable pageable) {
        Page<PostureEntity> entity = postureRepository.findByIsSample(isSample, pageable);
        PostureWithPageInfo postures = PostureEntity.toPostureWithPageInfo(entity);
        return postures;
    }

    @Transactional
    public PostureWithPageInfo getPosturesByUserId(Long userId, Pageable pageable) {
        Page<PostureEntity> entity = postureRepository.findByUserId(userId, pageable);
        PostureWithPageInfo postures = PostureEntity.toPostureWithPageInfo(entity);
        return postures;
    }

    @Transactional
    public Posture getRandomPosture() {
        List<PostureEntity> unannotatedPostures = postureRepository.findOrderByAnnotationCountLimitedTo(RANDOM_POOL);
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
        return PostureEntity.toPosture(target);
    }

    @Transactional
    public Posture getRandomPostureByAnnotaterId(Long annotaterId) {
        List<PostureEntity> unannotatedPostures = postureRepository
                .findByAnnotaterIdOrderByAnnotationCountLimitTo(annotaterId, RANDOM_POOL, MAX_NEED_COUNT);
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
        return PostureEntity.toPosture(target);
    }

    @Transactional
    public Posture getRandomPostureThinOutById(Long step) {
        List<PostureEntity> unannotatedPostures = postureRepository.findByOrderByAnnotationCountThinOutByIdLimitTo(
                step, RANDOM_POOL, MAX_NEED_COUNT);
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
        return PostureEntity.toPosture(target);
    }

    @Transactional
    public Posture getRandomPostureByAnnotaterIdThinOutById(Long annotaterId, Long step) {
        List<PostureEntity> unannotatedPostures = postureRepository
                .findByAnnotaterIdOrderByAnnotationCountThinOutByIdLimitTo(annotaterId, step, RANDOM_POOL,
                        MAX_NEED_COUNT);
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
        return PostureEntity.toPosture(target);
    }

    @Transactional
    public Posture updatePostureById(Long id, PostureUpdate postureUpdate) {
        int updateCount = postureRepository
                .updatePostureById(
                        id,
                        postureUpdate.getNeckAngle(),
                        postureUpdate.getTorsoAngle());
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
                postureUpdateWithFile.getAnnotaterId());
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
                posture.getAnnotaterId().toString()).toString();
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
                        postureUpdate.getImageHeight().get());
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
                            postureUpdate.getImageHeight().get());
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
                    .setIsSample(true));
        }
        List<PostureEntity> savedPostures = postureRepository.saveAll(samplePostures);
        return PostureEntity.toPostures(savedPostures);
    }

    @Transactional
    public Posture getRandomSamplePosture() {
        List<PostureEntity> unannotatedPostures = postureRepository
                .findSampleByOrderByAnnotationCountLimitedTo(RANDOM_POOL);
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
        return PostureEntity.toPosture(target);
    }

    @Transactional
    public Posture getRandomSamplePostureByAnnotaterId(Long annotaterId) {
        List<PostureEntity> unannotatedPostures = postureRepository
                .findSampleByAnnotaterIdOrderByAnnotationCountLimitTo(annotaterId, RANDOM_POOL, MAX_NEED_COUNT);
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
        return PostureEntity.toPosture(target);
    }

    @Transactional
    public PostureWithAnnotations getPostureWithAnnotationsById(Long id) {
        PostureEntity posture = postureRepository.findById(id).orElse(null);
        if (posture == null) {
            return null;
        }

        return PostureEntity.toPostureWithAnnotations(posture);
    }

}
