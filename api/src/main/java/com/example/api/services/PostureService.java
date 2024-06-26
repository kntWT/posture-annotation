package com.example.api.services;

import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import jakarta.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.PessimisticLockingFailureException;
// import org.springframework.data.jpa.domain.JpaSort.Path;

import com.example.api.repositories.PostureRepository;
import com.example.api.entities.PostureEntity;
import com.generated.model.Posture;
import com.generated.model.PostureUpdate;
import com.generated.model.PostureUpdateWithFile;
import com.generated.model.PostureUpdateMarkerPosition;

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
        String path = System.getenv("IMAGE_DIR");
        String fileName = formatter.format(posture.getExCreatedAt()) + ".jpg";
        Path dir = Paths.get(path, posture.getUserId().toString());
        if(!Files.exists(dir)) {
            try {
                Files.createDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String base64Image = postureUpdateWithFile.getFile();
            if (base64Image.startsWith("data:image/jpeg;base64,")) {
                base64Image = base64Image.replace("data:image/jpeg;base64,", "");
            }
            byte[] data = Base64.getDecoder().decode(base64Image);
            OutputStream out = new FileOutputStream(dir.toString() + "/" + fileName);            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return posture;
    }

    @Transactional
    public Posture updatePostureMarkerById(Long id, PostureUpdateMarkerPosition postureUpdate) {
        int updateCount = postureRepository
            .updatePostureById(
                id,
                postureUpdate.getTragusX(),
                postureUpdate.getTragusY(),
                postureUpdate.getShoulderX(),
                postureUpdate.getShoulderY(),
                postureUpdate.getWaistX(),
                postureUpdate.getWaistY(),
                postureUpdate.getImageWidth(),
                postureUpdate.getImageHeight()
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
                    postureUpdate.getId(),
                    postureUpdate.getTragusX(),
                    postureUpdate.getTragusY(),
                    postureUpdate.getShoulderX(),
                    postureUpdate.getShoulderY(),
                    postureUpdate.getWaistX(),
                    postureUpdate.getWaistY(),
                    postureUpdate.getImageWidth(),
                    postureUpdate.getImageHeight()
                );
            if (count > 0) {
                updatedCount++;
            }
        }
        return updatedCount;
    }

}
