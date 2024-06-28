package com.example.api.entities;

import org.hibernate.annotations.CreationTimestamp;
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
import com.generated.model.Posture;

@Entity
@Table(name = "postures")
public class PostureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "in_id", nullable = false)
    private Long inId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "set_num", nullable = false)
    private Long setNum;

    @Column(name = "orientation_alpha", nullable = false)
    private Double orientationAlpha;

    @Column(name = "orientation_beta", nullable = false)
    private Double orientationBeta;

    @Column(name = "orientation_gamma", nullable = false)
    private Double orientationGamma;

    @Column(name = "pitch", nullable = false)
    private Double pitch;

    @Column(name = "yaw", nullable = false)
    private Double yaw;

    @Column(name = "roll", nullable = false)
    private Double roll;

    @Column(name = "nose_x", nullable = false)
    private Double noseX;

    @Column(name = "nose_y", nullable = false)
    private Double noseY;

    @Column(name = "neck_x", nullable = false)
    private Double nexkX;

    @Column(name = "neck_y", nullable = false)
    private Double neckY;

    @Column(name = "neck_to_nose", nullable = false)
    private Double neckToNose;

    @Column(name = "standard_dist", nullable = false)
    private Double standardDist;

    @CreationTimestamp
    @Column(name = "in_created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime inCreatedAt;

    @CreationTimestamp
    @Column(name = "in_updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime inUpdatedAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "neck_to_nose_standard", nullable = false)
    private Double neckToNoseStandard;

    @Column(name = "neck_angle_offset", nullable = false)
    private Double neckAngleOffset;

    @Column(name = "ex_id", nullable = false)
    private Long exId;

    @Column(name = "neck_angle", nullable = false)
    private Double neckAngle;

    @Column(name = "torso_angle", nullable = false)
    private Double torsoAngle;

    @CreationTimestamp
    @Column(name = "ex_created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime exCreatedAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP")
    private OffsetDateTime updatedAt;

    @Column(name = "annotater_id", nullable = false)
    private Long annotaterId;

    @Column(name = "tragus_x", nullable = true)
    private Double tragusX;

    @Column(name = "tragus_y", nullable = true)
    private Double tragusY;

    @Column(name = "shoulder_x", nullable = true)
    private Double shoulderX;

    @Column(name = "shoulder_y", nullable = true)
    private Double shoulderY;

    @Column(name = "waist_x", nullable = true)
    private Double waistX;

    @Column(name = "waist_y", nullable = true)
    private Double waistY;

    @Column(name = "image_width", nullable = true)
    private Double imageWidth;

    @Column(name = "image_height", nullable = true)
    private Double imageHeight;

    public PostureEntity() {}

    public Long getId() {
        return id;
    }

    public PostureEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getInId() {
        return inId;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getSetNum() {
        return setNum;
    }

    public Double getOrientationAlpha() {
        return orientationAlpha;
    }

    public Double getOrientationBeta() {
        return orientationBeta;
    }

    public Double getOrientationGamma() {
        return orientationGamma;
    }

    public Double getPitch() {
        return pitch;
    }

    public Double getYaw() {
        return yaw;
    }

    public Double getRoll() {
        return roll;
    }

    public Double getNoseX() {
        return noseX;
    }

    public Double getNoseY() {
        return noseY;
    }

    public Double getNexkX() {
        return nexkX;
    }

    public Double getNeckY() {
        return neckY;
    }

    public Double getNeckToNose() {
        return neckToNose;
    }

    public Double getStandardDist() {
        return standardDist;
    }

    public OffsetDateTime getInCreatedAt() {
        return inCreatedAt;
    }

    public OffsetDateTime getInUpdatedAt() {
        return inUpdatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Double getNeckToNoseStandard() {
        return neckToNoseStandard;
    }

    public Double getNeckAngleOffset() {
        return neckAngleOffset;
    }

    public Long getExId() {
        return exId;
    }

    public Double getNeckAngle() {
        return neckAngle;
    }

    public PostureEntity setNeckAngle(Double neckAngle) {
        this.neckAngle = neckAngle;
        return this;
    }

    public Double getTorsoAngle() {
        return torsoAngle;
    }

    public PostureEntity setTorosAngle(Double torsoAngle) {
        this.torsoAngle = torsoAngle;
        return this;
    }

    public OffsetDateTime getExCreatedAt() {
        return exCreatedAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getAnnotaterId() {
        return annotaterId;
    }

    public PostureEntity setAnnotaterId(Long annotaterId) {
        this.annotaterId = annotaterId;
        return this;
    }

    public Double getTragusX() {
        return tragusX;
    }

    public PostureEntity setTragusX(Double tragusX) {
        this.tragusX = tragusX;
        return this;
    }

    public Double getTragusY() {
        return tragusY;
    }

    public PostureEntity setTragusY(Double tragusY) {
        this.tragusY = tragusY;
        return this;
    }

    public Double getShoulderX() {
        return shoulderX;
    }

    public PostureEntity setShoulderX(Double shoulderX) {
        this.shoulderX = shoulderX;
        return this;
    }

    public Double getShoulderY() {
        return shoulderY;
    }

    public PostureEntity setShoulderY(Double shoulderY) {
        this.shoulderY = shoulderY;
        return this;
    }

    public Double getWaistX() {
        return waistX;
    }

    public PostureEntity setWaistX(Double waistX) {
        this.waistX = waistX;
        return this;
    }

    public Double getWaistY() {
        return waistY;
    }

    public PostureEntity setWaistY(Double waistY) {
        this.waistY = waistY;
        return this;
    }

    public Double getImageWidth() {
        return imageWidth;
    }

    public PostureEntity setImageWidth(Double imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    public Double getImageHeight() {
        return imageHeight;
    }

    public PostureEntity setImageHeight(Double imageHeight) {
        this.imageHeight = imageHeight;
        return this;
    }

    public Posture toPosture() {
        return new Posture(
            this.getId(),
            this.getInId(),
            this.getFileName(),
            this.getSetNum(),
            this.getOrientationAlpha(),
            this.getOrientationBeta(),
            this.getOrientationGamma(),
            this.getPitch(),
            this.getYaw(),
            this.getRoll(),
            this.getNoseX(),
            this.getNoseY(),
            this.getNexkX(),
            this.getNeckY(),
            this.getNeckToNose(),
            this.getStandardDist(),
            this.getInCreatedAt(),
            this.getInUpdatedAt(),
            this.getUserId(),
            this.getName(),
            this.getPassword(),
            this.getNeckToNoseStandard(),
            this.getNeckAngleOffset(),
            this.getExId(),
            this.getExCreatedAt(),
            this.getUpdatedAt(),
            this.getNeckAngle(),
            this.getTorsoAngle(),
            this.getAnnotaterId()
        )
            .tragusX(this.getTragusX())
            .tragusY(this.getTragusY())
            .shoulderX(this.getShoulderX())
            .shoulderY(this.getShoulderY())
            .waistX(this.getWaistX())
            .waistY(this.getWaistY())
            .imageWidth(this.getImageWidth())
            .imageHeight(this.getImageHeight());
    }

    public static List<Posture> toPostures(List<PostureEntity> postures) {
        return postures.stream()
            .map(posture -> posture.toPosture())
            .collect(Collectors.toList());
    }

}
