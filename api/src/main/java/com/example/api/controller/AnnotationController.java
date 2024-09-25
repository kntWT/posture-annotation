package com.example.api.controller;

import java.util.List;
import com.generated.api.AnnotationApi;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<Annotation>> getAnnotations() {
        List<Annotation> annotations = annotationService.getAnnotations();
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
    public ResponseEntity<List<Annotation>> getAnnotationsByAnnotaterId(Long annotaterId) {
        List<Annotation> annotations = annotationService.getAnnotationsByAnnotaterId(annotaterId);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<List<AnnotationWithFilePath>> getAnnotationsWithFilePathByAnnotaterId(Long annotaterId) {
        List<AnnotationWithFilePath> annotations = annotationService
                .getAnnotationsWithFilePathByAnnotaterId(annotaterId);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<List<Annotation>> getAnnotationsByPostureId(Long postureId) {
        List<Annotation> annotations = annotationService.getAnnotationsByPostureId(postureId);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<List<AnnotationWithFilePath>> getAnnotationsWithFilePathByPostureId(Long postureId) {
        List<AnnotationWithFilePath> annotations = annotationService.getAnnotationsWithFilePathByPostureId(postureId);
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
    public ResponseEntity<List<Annotation>> getProdAnnotationsByAnnotaterId(Long annotaterId) {
        List<Annotation> annotations = annotationService.getProdAnnotationsByAnnotaterId(annotaterId);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<List<AnnotationWithFilePath>> getProdAnnotationsWithFilePathByAnnotaterId(Long annotaterId) {
        List<AnnotationWithFilePath> annotations = annotationService
                .getProdAnnotationsWithFilePathByAnnotaterId(annotaterId);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<Long> getProdAnnotationCountByAnnotaterId(Long annotaterId) {
        Long count = annotationService.getProdAnnotationCountByAnnotaterId(annotaterId);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<List<Annotation>> getSampleAnnotationsByAnnotaterId(Long annotaterId) {
        List<Annotation> annotations = annotationService.getSampleAnnotationsByAnnotaterId(annotaterId);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<List<AnnotationWithFilePath>> getSampleAnnotationsWithFilePathByAnnotaterId(
            Long annotaterId) {
        List<AnnotationWithFilePath> annotations = annotationService
                .getSampleAnnotationsWithFilePathByAnnotaterId(annotaterId);
        return ResponseEntity.ok(annotations);
    }

    @Override
    public ResponseEntity<Long> getSampleAnnotationCountByAnnotaterId(Long annotaterId) {
        Long count = annotationService.getSampleAnnotationCountByAnnotaterId(annotaterId);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<List<AnnotationSummaryByPosture>> getAnnotationSummaryByPosture() {
        List<AnnotationSummaryByPosture> annotationSummary = annotationService.getAnnotationSummaryByPosture();
        if (annotationSummary == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(annotationSummary);
    }
}
