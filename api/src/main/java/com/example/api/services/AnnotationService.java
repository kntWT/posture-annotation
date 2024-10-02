package com.example.api.services;

import java.util.List;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.example.api.repositories.AnnotationRepository;
import com.example.api.utils.DateFormatter;
import com.example.api.utils.SaveFile;
import com.example.api.entities.AnnotationEntity;
import com.example.api.entities.AnnotationSummaryByAnnotaterEntity;
import com.example.api.entities.AnnotationSummaryByPostureEntity;
import com.generated.model.Annotation;
import com.generated.model.AnnotationUpdate;
import com.generated.model.AnnotationUpdateWithFile;
import com.generated.model.AnnotationCreate;
import com.generated.model.AnnotationCreateWithFile;
import com.generated.model.AnnotationWithFilePath;
import com.generated.model.AnnotationSummaryByPosture;
import com.generated.model.AnnotationSummaryByAnnotater;
import com.generated.model.AnnotationWithPosture;

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
                annotationCreate.getTorsoAngle());

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
                annotationWithFile.getTorsoAngle());
        Annotation annotation = createAnnotation(annotationCreate);

        if (annotation == null) {
            return null;
        }

        String fileName = annotationWithFile.getFileName();
        String basePath = System.getenv("IMAGE_DIR");
        String subPath = annotationRepository.isSampleById(annotation.getId())
                ? "sample"
                : annotationWithFile.getUserId().toString();
        String dir = Paths.get(
                basePath,
                subPath,
                annotationWithFile.getAnnotaterId().toString()).toString();
        boolean success = SaveFile.saveBase64Image(fileName, annotationWithFile.getFile(), dir);
        if (!success) {
            return null;
        }

        return annotation;
    }

    @Transactional
    public List<Annotation> getAnnotations(Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findAllByOrderByIdAsc(pageable).getContent();
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
    public AnnotationWithPosture getAnnotationWithPostureById(Long id) {
        AnnotationEntity annotation = annotationRepository.findWithPostureById(id);
        if (annotation == null) {
            return null;
        }

        return AnnotationEntity.toAnnotationWithPosture(annotation);
    }

    @Transactional
    public AnnotationWithFilePath getAnnotationWithFilePathById(Long id) {
        AnnotationEntity annotation = annotationRepository.findWithFilePathById(id);
        if (annotation == null) {
            return null;
        }

        return annotation.toAnnotationWithFilePath();
    }

    @Transactional
    public List<Annotation> getAnnotationsById(List<Long> ids) {
        List<AnnotationEntity> annotations = annotationRepository.findAllByIdInOrderByIdAsc(ids);
        return AnnotationEntity.toAnnotations(annotations);
    }

    @Transactional
    public List<AnnotationWithFilePath> getAnnotationsWithFilePathById(List<Long> ids) {
        List<AnnotationEntity> annotations = annotationRepository.findAllByIdWithFilePathOrderByIdAsc(ids);
        return AnnotationEntity.toAnnotationsWithFilePath(annotations);
    }

    @Transactional
    public boolean isAnnotationExisById(Long id) {
        return annotationRepository.existsById(id);
    }

    @Transactional
    public boolean isAnnotationExistByPostureIdAndAnnotaterId(Long postureId, Long annotaterId) {
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
                annotationUpdate.getTorsoAngle());
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
                annotationWithFile.getTorsoAngle());
        Annotation annotation = updateAnnotationById(id, annotationUpdate);

        if (annotation == null) {
            return null;
        }

        String fileName = annotationWithFile.getFileName();
        String basePath = System.getenv("IMAGE_DIR");
        String subPath = annotationRepository.isSampleById(annotation.getId())
                ? "sample"
                : annotationWithFile.getUserId().toString();
        String dir = Paths.get(
                basePath,
                subPath,
                annotation.getAnnotaterId().toString()).toString();
        boolean success = SaveFile.saveBase64Image(fileName, annotationWithFile.getFile(), dir);
        if (!success) {
            return null;
        }

        return annotation;
    }

    @Transactional
    public List<Annotation> getAnnotationsByAnnotaterId(Long annotaterId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findByAnnotaterIdOrderByIdAsc(annotaterId, pageable)
                .getContent();
        return AnnotationEntity.toAnnotations(annotations);
    }

    @Transactional
    public List<AnnotationWithPosture> getAnnotationsWithPostureByAnnotaterId(Long annotaterId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findAllWithPostureByAnnotaterId(annotaterId, pageable)
                .getContent();
        if (annotations == null) {
            return null;
        }

        return AnnotationEntity.toAnnotationsWithPosture(annotations);
    }

    @Transactional
    public List<AnnotationWithFilePath> getAnnotationsWithFilePathByAnnotaterId(Long annotaterId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findByAnnotaterIdWithFilePath(annotaterId, pageable)
                .getContent();
        return AnnotationEntity.toAnnotationsWithFilePath(annotations);
    }

    @Transactional
    public Long getAnnotationCountByAnnotaterId(Long annotaterId) {
        return annotationRepository.countByAnnotaterId(annotaterId);
    }

    @Transactional
    public List<Annotation> getAnnotationsByPostureId(Long postureId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findByPostureIdOrderByIdAsc(postureId, pageable)
                .getContent();
        return AnnotationEntity.toAnnotations(annotations);
    }

    @Transactional
    public List<AnnotationWithFilePath> getAnnotationsWithFilePathByPostureId(Long postureId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findByPostureIdWithFilePath(postureId, pageable)
                .getContent();
        return AnnotationEntity.toAnnotationsWithFilePath(annotations);
    }

    @Transactional
    public List<AnnotationWithPosture> getAnnotationWithPostureByAnnotaterId(Long annotaterId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findAllWithPostureByAnnotaterId(annotaterId, pageable)
                .getContent();
        if (annotations == null) {
            return null;
        }

        return AnnotationEntity.toAnnotationsWithPosture(annotations);
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
        AnnotationEntity annotation = annotationRepository.findByPostureIdAndAnnotaterIdWithFilePath(postureId,
                annotaterId);
        if (annotation == null) {
            return null;
        }

        return annotation.toAnnotationWithFilePath();
    }

    @Transactional
    public Annotation updateAnnotationByPostureIdAndAnnotaterId(Long postureId, Long annotaterId,
            AnnotationUpdateWithFile annotationWithFile) {
        Annotation annotation = getAnnotationByPostureIdAndAnnotaterId(postureId, annotaterId);
        if (annotation == null) {
            return null;
        }

        return updateAnnotationByIdAndSaveFile(annotation.getId(), annotationWithFile);
    }

    @Transactional
    public List<Annotation> getProdAnnotationsByAnnotaterId(Long annotaterId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findProdByAnnotaterId(annotaterId, pageable)
                .getContent();
        return AnnotationEntity.toAnnotations(annotations);
    }

    @Transactional
    public List<AnnotationWithFilePath> getProdAnnotationsWithFilePathByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository
                .findProdByAnnotaterIdWithFilePath(annotaterId, pageable)
                .getContent();
        return AnnotationEntity.toAnnotationsWithFilePath(annotations);
    }

    @Transactional
    public List<AnnotationWithPosture> getProdAnnotationsWithPostureByAnnotaterId(Long annotaterId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository
                .findProdWithPostureByAnnotaterId(annotaterId, pageable)
                .getContent();
        if (annotations == null) {
            return null;
        }
        for (AnnotationEntity annotation : annotations) {
            System.out.println(DateFormatter.format(annotation.getPosture().getExCreatedAt()));
        }

        return AnnotationEntity.toAnnotationsWithPosture(annotations);
    }

    @Transactional
    public Long getProdAnnotationCountByAnnotaterId(Long annotaterId) {
        return annotationRepository.countByAnnotaterIdAndIsSample(annotaterId, false);
    }

    @Transactional
    public List<Annotation> getSampleAnnotationsByAnnotaterId(Long annotaterId, Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository.findSampleByAnnotaterId(annotaterId, pageable)
                .getContent();
        return AnnotationEntity.toAnnotations(annotations);
    }

    @Transactional
    public List<AnnotationWithFilePath> getSampleAnnotationsWithFilePathByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository
                .findSampleByAnnotaterIdWithFilePath(annotaterId, pageable)
                .getContent();
        return AnnotationEntity.toAnnotationsWithFilePath(annotations);
    }

    @Transactional
    public List<AnnotationWithPosture> getSampleAnnotationsWithPostureByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        List<AnnotationEntity> annotations = annotationRepository
                .findSampleWithPostureByAnnotaterId(annotaterId, pageable)
                .getContent();
        if (annotations == null) {
            return null;
        }

        return AnnotationEntity.toAnnotationsWithPosture(annotations);
    }

    @Transactional
    public Long getSampleAnnotationCountByAnnotaterId(Long annotaterId) {
        return annotationRepository.countByAnnotaterIdAndIsSample(annotaterId, true);
    }

    @Transactional
    public List<AnnotationSummaryByPosture> getAnnotationSummaryByPosture(Pageable pageable) {
        List<AnnotationSummaryByPostureEntity> annotationSummary = annotationRepository
                .getAnnotationSummaryByPosture(pageable);
        if (annotationSummary.size() <= 0) {
            return null;
        }

        return AnnotationSummaryByPostureEntity.toAnnotationSummaryByPostures(annotationSummary);
    }

    @Transactional
    public List<AnnotationSummaryByAnnotater> getAnnotationSummaryByAnnotater(Pageable pageable) {
        List<AnnotationSummaryByAnnotaterEntity> annotationSummary = annotationRepository
                .getAnnotationSummaryByAnnotater(pageable);
        if (annotationSummary.size() <= 0) {
            return null;
        }

        return AnnotationSummaryByAnnotaterEntity.toAnnotationSummaryByAnnotaters(annotationSummary);
    }

}
