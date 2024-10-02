package com.example.api.services;

import java.util.List;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
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
import com.generated.model.AnnotationWithPageInfo;
import com.generated.model.AnnotationWithFilePathAndPageInfo;
import com.generated.model.AnnotationWithPostureAndPageInfo;
import com.generated.model.AnnotationSummaryByPostureWithPageInfo;
import com.generated.model.AnnotationSummaryByAnnotaterWithPageInfo;

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

        return AnnotationEntity.toAnnotation(annotationRepository.save(annotation));
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
    public AnnotationWithPageInfo getAnnotations(Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findAllByOrderByIdAsc(pageable);
        AnnotationWithPageInfo annotations = AnnotationEntity.toAnnotationWithPageInfo(entity);
        return annotations;
    }

    @Transactional
    public Annotation getAnnotationById(Long id) {
        AnnotationEntity annotation = annotationRepository.findById(id).orElse(null);
        if (annotation == null) {
            return null;
        }

        return AnnotationEntity.toAnnotation(annotation);
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
        AnnotationEntity annotation = annotationRepository.findByIdWithFilePath(id);
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

        return AnnotationEntity.toAnnotation(annotation);
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
    public AnnotationWithPageInfo getAnnotationsByAnnotaterId(Long annotaterId, Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findByAnnotaterIdOrderByIdAsc(annotaterId, pageable);
        AnnotationWithPageInfo annotations = AnnotationEntity.toAnnotationWithPageInfo(entity);
        return annotations;
    }

    @Transactional
    public AnnotationWithPostureAndPageInfo getAnnotationsWithPostureByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findAllWithPostureByAnnotaterId(annotaterId,
                pageable);
        if (entity == null) {
            return null;
        }
        AnnotationWithPostureAndPageInfo annotations = AnnotationEntity.toAnnotationWithPostureAndPageInfo(entity);

        return annotations;
    }

    @Transactional
    public AnnotationWithFilePathAndPageInfo getAnnotationsWithFilePathByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findByAnnotaterIdWithFilePath(annotaterId, pageable);
        AnnotationWithFilePathAndPageInfo annotations = AnnotationEntity.toAnnotationWithFilePathAndPageInfo(entity);
        return annotations;
    }

    @Transactional
    public Long getAnnotationCountByAnnotaterId(Long annotaterId) {
        return annotationRepository.countByAnnotaterId(annotaterId);
    }

    @Transactional
    public AnnotationWithPageInfo getAnnotationsByPostureId(Long postureId, Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findByPostureIdOrderByIdAsc(postureId, pageable);
        AnnotationWithPageInfo annotations = AnnotationEntity.toAnnotationWithPageInfo(entity);
        return annotations;
    }

    @Transactional
    public AnnotationWithFilePathAndPageInfo getAnnotationsWithFilePathByPostureId(Long postureId, Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findByPostureIdWithFilePath(postureId, pageable);
        AnnotationWithFilePathAndPageInfo annotations = AnnotationEntity.toAnnotationWithFilePathAndPageInfo(entity);
        return annotations;
    }

    @Transactional
    public AnnotationWithPostureAndPageInfo getAnnotationWithPostureByAnnotaterId(Long annotaterId, Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findAllWithPostureByAnnotaterId(annotaterId, pageable);
        if (entity == null) {
            return null;
        }
        AnnotationWithPostureAndPageInfo annotations = AnnotationEntity.toAnnotationWithPostureAndPageInfo(entity);
        return annotations;
    }

    @Transactional
    public Annotation getAnnotationByPostureIdAndAnnotaterId(Long postureId, Long annotaterId) {
        AnnotationEntity annotation = annotationRepository.findByPostureIdAndAnnotaterId(postureId, annotaterId);
        if (annotation == null) {
            return null;
        }

        return AnnotationEntity.toAnnotation(annotation);
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
    public AnnotationWithPageInfo getProdAnnotationsByAnnotaterId(Long annotaterId, Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findProdByAnnotaterId(annotaterId, pageable);
        AnnotationWithPageInfo annotations = AnnotationEntity.toAnnotationWithPageInfo(entity);
        return annotations;
    }

    @Transactional
    public AnnotationWithFilePathAndPageInfo getProdAnnotationsWithFilePathByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository
                .findProdByAnnotaterIdWithFilePath(annotaterId, pageable);
        AnnotationWithFilePathAndPageInfo annotations = AnnotationEntity.toAnnotationWithFilePathAndPageInfo(entity);
        return annotations;
    }

    @Transactional
    public AnnotationWithPostureAndPageInfo getProdAnnotationsWithPostureByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository
                .findProdWithPostureByAnnotaterId(annotaterId, pageable);
        if (entity == null) {
            return null;
        }
        AnnotationWithPostureAndPageInfo annotations = AnnotationEntity.toAnnotationWithPostureAndPageInfo(entity);
        return annotations;
    }

    @Transactional
    public Long getProdAnnotationCountByAnnotaterId(Long annotaterId) {
        return annotationRepository.countByAnnotaterIdAndIsSample(annotaterId, false);
    }

    @Transactional
    public AnnotationWithPageInfo getSampleAnnotationsByAnnotaterId(Long annotaterId, Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository.findSampleByAnnotaterId(annotaterId, pageable);
        AnnotationWithPageInfo annotations = AnnotationEntity.toAnnotationWithPageInfo(entity);
        return annotations;
    }

    @Transactional
    public AnnotationWithFilePathAndPageInfo getSampleAnnotationsWithFilePathByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository
                .findSampleByAnnotaterIdWithFilePath(annotaterId, pageable);
        AnnotationWithFilePathAndPageInfo annotations = AnnotationEntity.toAnnotationWithFilePathAndPageInfo(entity);
        return annotations;
    }

    @Transactional
    public AnnotationWithPostureAndPageInfo getSampleAnnotationsWithPostureByAnnotaterId(Long annotaterId,
            Pageable pageable) {
        Page<AnnotationEntity> entity = annotationRepository
                .findSampleWithPostureByAnnotaterId(annotaterId, pageable);
        if (entity == null) {
            return null;
        }
        AnnotationWithPostureAndPageInfo annotations = AnnotationEntity.toAnnotationWithPostureAndPageInfo(entity);
        return annotations;
    }

    @Transactional
    public Long getSampleAnnotationCountByAnnotaterId(Long annotaterId) {
        return annotationRepository.countByAnnotaterIdAndIsSample(annotaterId, true);
    }

    @Transactional
    public AnnotationSummaryByPostureWithPageInfo getAnnotationSummaryByPosture(Pageable pageable) {
        AnnotationSummaryByPostureWithPageInfo annotationSummary = annotationRepository
                .getAnnotationSummaryByPosture(pageable);
        return annotationSummary;
    }

    @Transactional
    public AnnotationSummaryByAnnotaterWithPageInfo getAnnotationSummaryByAnnotater(Pageable pageable) {
        AnnotationSummaryByAnnotaterWithPageInfo annotationSummary = annotationRepository
                .getAnnotationSummaryByAnnotater(pageable);
        return annotationSummary;
    }

}
