package com.example.api.services;

import java.util.List;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.example.api.repositories.AnnotationRepository;
import com.example.api.utils.DateFormatter;
import com.example.api.utils.SaveFile;
import com.example.api.entities.AnnotationEntity;
import com.generated.model.Annotation;
import com.generated.model.AnnotationUpdate;
import com.generated.model.AnnotationUpdateWithFile;
import com.generated.model.AnnotationCreate;
import com.generated.model.AnnotationCreateWithFile;
import com.generated.model.AnnotationWithFilePath;
import com.example.api.entities.PostureEntity;

@Service
public class AnnotationService {

    @Autowired
    private final AnnotationRepository annotationRepository;

    public AnnotationService(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    @Transactional
    public Annotation createAnnotation(AnnotationCreate annotationCreate) {
        Long postureId = annotationCreate.getPostureId();
        Long annotaterId = annotationCreate.getAnnotaterId();
        if (isAnnotationExistByPostureIdAndAnnotaterId(annotaterId, postureId)) {
            return null;
        }

        AnnotationEntity annotation = new AnnotationEntity(
            postureId,
            annotaterId,
            annotationCreate.getTragusX(),
            annotationCreate.getTragusY(),
            annotationCreate.getShoulderX(),
            annotationCreate.getShoulderY(),
            annotationCreate.getWaistX(),
            annotationCreate.getWaistY(),
            annotationCreate.getNeckAngle(),
            annotationCreate.getTorsoAngle()
        );

        return annotationRepository.save(annotation).toAnnotation();
    }

    @Transactional
    public Annotation createAnnotationAndSaveFile(AnnotationCreateWithFile annotationWithFile) {
        AnnotationCreate annotationCreate = new AnnotationCreate(
            annotationWithFile.getPostureId(),
            annotationWithFile.getAnnotaterId(),
            annotationWithFile.getTragusX(),
            annotationWithFile.getTragusY(),
            annotationWithFile.getShoulderX(),
            annotationWithFile.getShoulderY(),
            annotationWithFile.getWaistX(),
            annotationWithFile.getWaistY(),
            annotationWithFile.getNeckAngle(),
            annotationWithFile.getTorsoAngle()
        );
        Annotation annotation = createAnnotation(annotationCreate);

        if (annotation == null) {
            return null;
        }

        String fileName = annotationWithFile.getFileName();
        String basePath = System.getenv("IMAGE_DIR");
        String dir = Paths.get(basePath, annotationWithFile.getUserId().toString()).toString();
        boolean success = SaveFile.saveBase64Image(fileName, annotationWithFile.getFile(), dir);
        if (!success) {
            return null;
        }

        return annotation;
    }

    @Transactional
    public List<Annotation> getAnnotations() {
        List<AnnotationEntity> annotations = annotationRepository.findAllByOrderByIdAsc();
        return AnnotationEntity.toAnnotations(annotations);
    }

    @Transactional
    public Annotation getAnnotationById(Long id) {
        AnnotationEntity annotation = annotationRepository.findById(id).orElse(null);
        if (annotation == null) {
            return null;
        }

        return annotation.toAnnotation();
    }

    @Transactional
    public AnnotationWithFilePath getAnnotationWithFilePathById(Long id) {
        AnnotationEntity annotation = annotationRepository.findByIdWithFilePath(id);
        if (annotation == null) {
            return null;
        }
        
        return annotation.toAnnotationWithFilePath();
    }

    @Transactional
    public boolean isAnnotationExist(Long id) {
        return annotationRepository.existsById(id);
    }

    @Transactional
    public boolean isAnnotationExistByPostureIdAndAnnotaterId(Long annotaterId, Long postureId) {
        return annotationRepository.existsByPostureIdAndAnnotaterId(postureId, annotaterId);
    }

    @Transactional
    public Annotation updateAnnotationById(Long id, AnnotationUpdate annotationUpdate) {
        int count = annotationRepository.updateAnnotationById(
            id,
            annotationUpdate.getTragusX(),
            annotationUpdate.getTragusY(),
            annotationUpdate.getShoulderX(),
            annotationUpdate.getShoulderY(),
            annotationUpdate.getWaistX(),
            annotationUpdate.getWaistY(),
            annotationUpdate.getNeckAngle(),
            annotationUpdate.getTorsoAngle()
        );
        if (count <= 0) {
            return null;
        }
        AnnotationEntity annotation = annotationRepository.findById(id).orElse(null);
        if (annotation == null) {
            return null;
        }

        return annotation.toAnnotation();
    }

    @Transactional
    public Annotation updateAnnotationByIdAndSaveFile(Long id, AnnotationUpdateWithFile annotationWithFile) {
        AnnotationUpdate annotationUpdate = new AnnotationUpdate(
            annotationWithFile.getTragusX(),
            annotationWithFile.getTragusY(),
            annotationWithFile.getShoulderX(),
            annotationWithFile.getShoulderY(),
            annotationWithFile.getWaistX(),
            annotationWithFile.getWaistY(),
            annotationWithFile.getNeckAngle(),
            annotationWithFile.getTorsoAngle()
        );
        Annotation annotation = updateAnnotationById(id, annotationUpdate);

        if (annotation == null) {
            return null;
        }

        String fileName = annotationWithFile.getFileName();
        String basePath = System.getenv("IMAGE_DIR");
        String dir = Paths.get(basePath, annotationWithFile.getUserId().toString()).toString();
        boolean success = SaveFile.saveBase64Image(fileName, annotationWithFile.getFile(), dir);
        if (!success) {
            return null;
        }

        return annotation;
    }

    @Transactional
    public List<Annotation> getAnnotationsByAnnotaterId(Long annotaterId) {
        List<AnnotationEntity> annotations = annotationRepository.findByAnnotaterIdOrderByIdAsc(annotaterId);
        return AnnotationEntity.toAnnotations(annotations);
    }

    @Transactional
    public List<AnnotationWithFilePath> getAnnotationsWithFilePathByAnnotaterId(Long annotaterId) {
        List<AnnotationEntity> annotations = annotationRepository.findByAnnotaterIdWithFilePath(annotaterId);
        return AnnotationEntity.toAnnotationsWithFilePath(annotations);
    }

    @Transactional
    public Long getAnnotationCountByAnnotaterId(Long annotaterId) {
        return annotationRepository.countByAnnotaterId(annotaterId);
    }

    @Transactional
    public List<Annotation> getAnnotationsByPostureId(Long postureId) {
        List<AnnotationEntity> annotations = annotationRepository.findByPostureIdOrderByIdAsc(postureId);
        return AnnotationEntity.toAnnotations(annotations);
    }

    @Transactional
    public List<AnnotationWithFilePath> getAnnotationsWithFilePathByPostureId(Long postureId) {
        List<AnnotationEntity> annotations = annotationRepository.findByPostureIdWithFilePath(postureId);
        return AnnotationEntity.toAnnotationsWithFilePath(annotations);
    }

    @Transactional
    public Annotation getAnnotationByPostureIdAndAnnotaterId(Long postureId, Long annotaterId) {
        AnnotationEntity annotation = annotationRepository.findByPostureIdAndAnnotaterId(postureId, annotaterId);
        if (annotation == null) {
            return null;
        }

        return annotation.toAnnotation();
    }

    @Transactional
    public AnnotationWithFilePath getAnnotationWithFilePathByPostureIdAndAnnotaterId(Long postureId, Long annotaterId) {
        AnnotationEntity annotation = annotationRepository.findByPostureIdAndAnnotaterIdWithFilePath(postureId, annotaterId);
        if (annotation == null) {
            return null;
        }

        return annotation.toAnnotationWithFilePath();
    }

    @Transactional
    public Annotation updateAnnotationByPostureIdAndAnnotaterId(Long postureId, Long annotaterId, AnnotationUpdateWithFile annotationWithFile) {
        Annotation annotation = getAnnotationByPostureIdAndAnnotaterId(postureId, annotaterId);
        if (annotation == null) {
            return null;
        }

        return updateAnnotationByIdAndSaveFile(annotation.getId(), annotationWithFile);
    }

}
