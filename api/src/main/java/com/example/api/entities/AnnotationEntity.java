package com.example.api.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.generated.model.Annotation;

@Entity
@Table(name = "annotations")
public class AnnotationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "posture_id", nullable = false)
    private Long postureId;

    @Column(name = "annotater_id", nullable = false)
    private Long annotaterId;

    @Column(name = "tragus_x", nullable = false)
    private Double tragusX;

    @Column(name = "tragus_y", nullable = false)
    private Double tragusY;

    @Column(name = "shoulder_x", nullable = false)
    private Double shoulderX;

    @Column(name = "shoulder_y", nullable = false)
    private Double shoulderY;

    @Column(name = "waist_x", nullable = false)
    private Double waistX;

    @Column(name = "waist_y", nullable = false)
    private Double waistY;

    @Column(name = "neck_angle", nullable = false)
    private Double neckAngle;

    @Column(name = "torso_angle", nullable = false)
    private Double torsoAngle;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private OffsetDateTime updatedAt;

    AnnotationEntity() {}

    public AnnotationEntity(
        Long postureId,
        Long annotaterId,
        Double tragusX,
        Double tragusY,
        Double shoulderX,
        Double shoulderY,
        Double waistX,
        Double waistY,
        Double neckAngle,
        Double torsoAngle
    ) {
        this.postureId = postureId;
        this.annotaterId = annotaterId;
        this.tragusX = tragusX;
        this.tragusY = tragusY;
        this.shoulderX = shoulderX;
        this.shoulderY = shoulderY;
        this.waistX = waistX;
        this.waistY = waistY;
        this.neckAngle = neckAngle;
        this.torsoAngle = torsoAngle;
    }

    public Long getId() {
        return id;
    }

    public Long getPostureId() {
        return postureId;
    }

    public Long getAnnotaterId() {
        return annotaterId;
    }

    public Double getTragusX() {
        return tragusX;
    }

    public Double getTragusY() {
        return tragusY;
    }

    public Double getShoulderX() {
        return shoulderX;
    }

    public Double getShoulderY() {
        return shoulderY;
    }

    public Double getWaistX() {
        return waistX;
    }

    public Double getWaistY() {
        return waistY;
    }

    public Annotation toAnnotation() {
        return new Annotation()
            .id(id)
            .postureId(postureId)
            .annotaterId(annotaterId)
            .tragusX(tragusX)
            .tragusY(tragusY)
            .shoulderX(shoulderX)
            .shoulderY(shoulderY)
            .waistX(waistX)
            .waistY(waistY);
    }

    public static List<Annotation> toAnnotations(List<AnnotationEntity> annotationEntities) {
        return annotationEntities.stream()
            .map(AnnotationEntity::toAnnotation)
            .collect(Collectors.toList());
    }

}
