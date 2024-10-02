package com.example.api.controller;

import java.util.List;
import com.generated.api.AnnotationApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.api.entities.AnnotationEntity;
import com.example.api.repositories.AnnotationRepository;
import com.example.api.services.AnnotationService;
import com.generated.api.AnnotationApi;
import com.generated.model.Annotation;
import com.generated.model.AnnotationUpdate;
import com.generated.model.AnnotationCreateWithFile;
import com.generated.model.AnnotationUpdateWithFile;
import com.generated.model.AnnotationWithFilePath;
import com.generated.model.AnnotationSummaryByPosture;
import com.generated.model.AnnotationSummaryByAnnotater;
import com.generated.model.AnnotationWithPosture;
import com.generated.model.AnnotationWithPageInfo;
import com.generated.model.AnnotationWithFilePathAndPageInfo;
import com.generated.model.AnnotationWithPostureAndPageInfo;
import com.generated.model.AnnotationSummaryByPostureWithPageInfo;
import com.generated.model.AnnotationSummaryByAnnotaterWithPageInfo;

@RestController
public class AnnotationController implements AnnotationApi {

    @Autowired
    private AnnotationRepository annotationRepository;
    @Autowired
    private AnnotationService annotationService;

    public AnnotationController(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    @Override
    public ResponseEntity<Annotation> createOrUpdateAnnotation(AnnotationCreateWithFile annotationWithFile) {
        boolean isExist = annotationService.isAnnotationExistByPostureIdAndAnnotaterId(
                annotationWithFile.getPostureId(),
                annotationWithFile.getAnnotaterId());
        if (isExist) {
            AnnotationUpdateWithFile annotationUpdate = new AnnotationUpdateWithFile(
                    annotationWithFile.getTragusX(),
                    annotationWithFile.getTragusY(),
                    annotationWithFile.getShoulderX(),
                    annotationWithFile.getShoulderY(),
                    annotationWithFile.getWaistX(),
                    annotationWithFile.getWaistY(),
                    annotationWithFile.getNeckAngle(),
                    annotationWithFile.getTorsoAngle(),
                    annotationWithFile.getFile(),
                    annotationWithFile.getAnnotaterId(),
                    annotationWithFile.getFileName());
            Annotation annotation = annotationService.updateAnnotationByPostureIdAndAnnotaterId(
                    annotationWithFile.getPostureId(),
                    annotationWithFile.getAnnotaterId(),
                    annotationUpdate);
            if (annotation == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok(annotation);
        }

        Annotation annotation = annotationService.createAnnotationAndSaveFile(annotationWithFile);
        if (annotation == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(annotation);
    }

    @Override
    public ResponseEntity<AnnotationWithPageInfo> getAnnotations(Long page, Long size, Pageable pageable) {
        AnnotationWithPageInfo annotations = annotationService.getAnnotations(pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<Annotation> getAnnotationById(Long id) {
        Annotation annotation = annotationService.getAnnotationById(id);
        if (annotation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(annotation);
    }

    @Override
    public ResponseEntity<AnnotationWithPosture> getAnnotationWithPostureById(Long id) {
        AnnotationWithPosture annotationWithPosture = annotationService.getAnnotationWithPostureById(id);
        if (annotationWithPosture == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(annotationWithPosture);
    }

    @Override
    public ResponseEntity<AnnotationWithFilePath> getAnnotationWithFilePathById(Long id) {
        AnnotationWithFilePath annotationWithFilePath = annotationService.getAnnotationWithFilePathById(id);
        if (annotationWithFilePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(annotationWithFilePath);
    }

    @Override
    public ResponseEntity<List<Annotation>> getAnnotationsById(List<Long> ids) {
        List<Annotation> annotations = annotationService.getAnnotationsById(ids);
        if (annotations == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<List<AnnotationWithFilePath>> getAnnotationsWithFilePathById(List<Long> ids) {
        List<AnnotationWithFilePath> annotations = annotationService.getAnnotationsWithFilePathById(ids);
        if (annotations == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<Annotation> updateAnnotationById(Long id, AnnotationUpdateWithFile annotationUpdate) {
        Annotation annotation = annotationService.updateAnnotationByIdAndSaveFile(id, annotationUpdate);
        if (annotation == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(annotation);
    }

    @Override
    public ResponseEntity<AnnotationWithPageInfo> getAnnotationsByAnnotaterId(Long annotaterId, Long page, Long size,
            Pageable pageable) {
        AnnotationWithPageInfo annotations = annotationService.getAnnotationsByAnnotaterId(annotaterId, pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<AnnotationWithPostureAndPageInfo> getAnnotationsWithPostureByAnnotaterId(Long annotaterId,
            Long page, Long size, Pageable pageable) {
        AnnotationWithPostureAndPageInfo annotations = annotationService.getAnnotationsWithPostureByAnnotaterId(
                annotaterId,
                pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<AnnotationWithFilePathAndPageInfo> getAnnotationsWithFilePathByAnnotaterId(Long annotaterId,
            Long page, Long size, Pageable pageable) {
        AnnotationWithFilePathAndPageInfo annotations = annotationService
                .getAnnotationsWithFilePathByAnnotaterId(annotaterId, pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<AnnotationWithPageInfo> getAnnotationsByPostureId(Long postureId, Long page, Long size,
            Pageable pageable) {
        AnnotationWithPageInfo annotations = annotationService.getAnnotationsByPostureId(postureId, pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<AnnotationWithFilePathAndPageInfo> getAnnotationsWithFilePathByPostureId(Long postureId,
            Long page, Long size, Pageable pageable) {
        AnnotationWithFilePathAndPageInfo annotations = annotationService.getAnnotationsWithFilePathByPostureId(
                postureId,
                pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<Long> getAnnotationCountByAnnotaterId(Long annotaterId) {
        Long count = annotationService.getAnnotationCountByAnnotaterId(annotaterId);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<Annotation> getAnnotationByPostureIdAndAnnotaterId(Long postureId, Long annotaterId) {
        Annotation annotation = annotationService.getAnnotationByPostureIdAndAnnotaterId(postureId, annotaterId);
        if (annotation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(annotation);
    }

    @Override
    public ResponseEntity<AnnotationWithFilePath> getAnnotationWithFilePathByPostureIdAndAnnotaterId(Long postureId,
            Long annotaterId) {
        AnnotationWithFilePath annotationWithFilePath = annotationService
                .getAnnotationWithFilePathByPostureIdAndAnnotaterId(postureId, annotaterId);
        if (annotationWithFilePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(annotationWithFilePath);
    }

    @Override
    public ResponseEntity<Annotation> updateAnnotationByPostureIdAndAnnotaterId(Long postureId, Long annotaterId,
            AnnotationUpdateWithFile annotationWithFile) {
        Annotation annotation = annotationService.updateAnnotationByPostureIdAndAnnotaterId(postureId, annotaterId,
                annotationWithFile);
        if (annotation == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(annotation);
    }

    @Override
    public ResponseEntity<AnnotationWithPageInfo> getProdAnnotationsByAnnotaterId(Long annotaterId, Long page,
            Long size,
            Pageable pageable) {
        AnnotationWithPageInfo annotations = annotationService.getProdAnnotationsByAnnotaterId(annotaterId, pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<AnnotationWithFilePathAndPageInfo> getProdAnnotationsWithFilePathByAnnotaterId(
            Long annotaterId,
            Long page, Long size, Pageable pageable) {
        AnnotationWithFilePathAndPageInfo annotations = annotationService
                .getProdAnnotationsWithFilePathByAnnotaterId(annotaterId, pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<AnnotationWithPostureAndPageInfo> getProdAnnotationsWithPostureByAnnotaterId(Long annotaterId,
            Long page,
            Long size,
            Pageable pageable) {
        AnnotationWithPostureAndPageInfo annotations = annotationService.getProdAnnotationsWithPostureByAnnotaterId(
                annotaterId,
                pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<Long> getProdAnnotationCountByAnnotaterId(Long annotaterId) {
        Long count = annotationService.getProdAnnotationCountByAnnotaterId(annotaterId);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<AnnotationWithPageInfo> getSampleAnnotationsByAnnotaterId(Long annotaterId, Long page,
            Long size,
            Pageable pageable) {
        AnnotationWithPageInfo annotations = annotationService.getSampleAnnotationsByAnnotaterId(annotaterId, pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<AnnotationWithFilePathAndPageInfo> getSampleAnnotationsWithFilePathByAnnotaterId(
            Long annotaterId, Long page, Long size, Pageable pageable) {
        AnnotationWithFilePathAndPageInfo annotations = annotationService
                .getSampleAnnotationsWithFilePathByAnnotaterId(annotaterId, pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<AnnotationWithPostureAndPageInfo> getSampleAnnotationsWithPostureByAnnotaterId(
            Long annotaterId,
            Long page, Long size, Pageable pageable) {
        AnnotationWithPostureAndPageInfo annotations = annotationService
                .getSampleAnnotationsWithPostureByAnnotaterId(annotaterId, pageable);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<Long> getSampleAnnotationCountByAnnotaterId(Long annotaterId) {
        Long count = annotationService.getSampleAnnotationCountByAnnotaterId(annotaterId);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<AnnotationSummaryByPostureWithPageInfo> getAnnotationSummaryByPosture(Long page, Long size,
            Pageable pageable) {
        AnnotationSummaryByPostureWithPageInfo annotationSummary = annotationService
                .getAnnotationSummaryByPosture(pageable);
        if (annotationSummary == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(annotationSummary);
    }

    @Override
    public ResponseEntity<AnnotationSummaryByAnnotaterWithPageInfo> getAnnotationSummaryByAnnotater(Long page,
            Long size,
            Pageable pageable) {
        AnnotationSummaryByAnnotaterWithPageInfo annotationSummary = annotationService
                .getAnnotationSummaryByAnnotater(pageable);
        if (annotationSummary == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(annotationSummary);
    }
}
