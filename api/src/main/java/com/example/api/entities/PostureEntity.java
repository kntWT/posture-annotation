package com.example.api.entities;

import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.generated.model.Posture;
import com.generated.model.PostureWithAnnotations;
import com.example.api.entities.AnnotationEntity;
import com.generated.model.Annotation;

@Entity
@NamedEntityGraph(name = "Posture.annotations", attributeNodes = {
        @NamedAttributeNode(value = "annotations")
})
@Table(name = "postures")
public class PostureEntity {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Double neckX;

    @Column(name = "neck_y", nullable = false)
    private Double neckY;

    @Column(name = "neck_to_nose", nullable = false)
    private Double neckToNose;

    @Column(name = "standard_dist", nullable = false)
    private Double standardDist;

    @Column(name = "in_created_at", nullable = false)
    private OffsetDateTime inCreatedAt;

    @Column(name = "in_updated_at", nullable = false)
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

    @Column(name = "ex_created_at", nullable = false)
    private OffsetDateTime exCreatedAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP")
    private OffsetDateTime updatedAt;

    @Column(name = "is_sample", nullable = false)
    private Boolean isSample;

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

    @OneToMany(mappedBy = "posture", fetch = FetchType.LAZY)
    private List<AnnotationEntity> annotations;

    public PostureEntity() {
    }

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

    public PostureEntity setInId(Long inId) {
        this.inId = inId;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public PostureEntity setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Long getSetNum() {
        return setNum;
    }

    public PostureEntity setSetNum(Long setNum) {
        this.setNum = setNum;
        return this;
    }

    public Double getOrientationAlpha() {
        return orientationAlpha;
    }

    public PostureEntity setOrientationAlpha(Double orientationAlpha) {
        this.orientationAlpha = orientationAlpha;
        return this;
    }

    public Double getOrientationBeta() {
        return orientationBeta;
    }

    public PostureEntity setOrientationBeta(Double orientationBeta) {
        this.orientationBeta = orientationBeta;
        return this;
    }

    public Double getOrientationGamma() {
        return orientationGamma;
    }

    public PostureEntity setOrientationGamma(Double orientationGamma) {
        this.orientationGamma = orientationGamma;
        return this;
    }

    public Double getPitch() {
        return pitch;
    }

    public PostureEntity setPitch(Double pitch) {
        this.pitch = pitch;
        return this;
    }

    public Double getYaw() {
        return yaw;
    }

    public PostureEntity setYaw(Double yaw) {
        this.yaw = yaw;
        return this;
    }

    public Double getRoll() {
        return roll;
    }

    public PostureEntity setRoll(Double roll) {
        this.roll = roll;
        return this;
    }

    public Double getNoseX() {
        return noseX;
    }

    public PostureEntity setNoseX(Double noseX) {
        this.noseX = noseX;
        return this;
    }

    public Double getNoseY() {
        return noseY;
    }

    public PostureEntity setNoseY(Double noseY) {
        this.noseY = noseY;
        return this;
    }

    public Double getNeckX() {
        return neckX;
    }

    public PostureEntity setNeckX(Double neckX) {
        this.neckX = neckX;
        return this;
    }

    public Double getNeckY() {
        return neckY;
    }

    public PostureEntity setNeckY(Double neckY) {
        this.neckY = neckY;
        return this;
    }

    public Double getNeckToNose() {
        return neckToNose;
    }

    public PostureEntity setNeckToNose(Double neckToNose) {
        this.neckToNose = neckToNose;
        return this;
    }

    public Double getStandardDist() {
        return standardDist;
    }

    public PostureEntity setStandardDist(Double standardDist) {
        this.standardDist = standardDist;
        return this;
    }

    public OffsetDateTime getInCreatedAt() {
        return inCreatedAt;
    }

    public PostureEntity setInCreatedAt(OffsetDateTime inCreatedAt) {
        this.inCreatedAt = inCreatedAt;
        return this;
    }

    public OffsetDateTime getInUpdatedAt() {
        return inUpdatedAt;
    }

    public PostureEntity setInUpdatedAt(OffsetDateTime inUpdatedAt) {
        this.inUpdatedAt = inUpdatedAt;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public PostureEntity setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public PostureEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PostureEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public Double getNeckToNoseStandard() {
        return neckToNoseStandard;
    }

    public PostureEntity setNeckToNoseStandard(Double neckToNoseStandard) {
        this.neckToNoseStandard = neckToNoseStandard;
        return this;
    }

    public Double getNeckAngleOffset() {
        return neckAngleOffset;
    }

    public PostureEntity setNeckAngleOffset(Double neckAngleOffset) {
        this.neckAngleOffset = neckAngleOffset;
        return this;
    }

    public Long getExId() {
        return exId;
    }

    public PostureEntity setExId(Long exId) {
        this.exId = exId;
        return this;
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

    public PostureEntity setTorsoAngle(Double torsoAngle) {
        this.torsoAngle = torsoAngle;
        return this;
    }

    public OffsetDateTime getExCreatedAt() {
        return exCreatedAt;
    }

    public PostureEntity setExCreatedAt(OffsetDateTime exCreatedAt) {
        this.exCreatedAt = exCreatedAt;
        return this;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PostureEntity setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Boolean getIsSample() {
        return isSample;
    }

    public PostureEntity setIsSample(Boolean isSample) {
        this.isSample = isSample;
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

    public List<AnnotationEntity> getAnnotations() {
        return annotations;
    }

    public Posture toPosture() {
        return new Posture()
                .id(this.getId())
                .inId(this.getInId())
                .fileName(this.getFileName())
                .setNum(this.getSetNum())
                .orientationAlpha(this.getOrientationAlpha())
                .orientationBeta(this.getOrientationBeta())
                .orientationGamma(this.getOrientationGamma())
                .pitch(this.getPitch())
                .yaw(this.getYaw())
                .roll(this.getRoll())
                .noseX(this.getNoseX())
                .noseY(this.getNoseY())
                .neckX(this.getNeckX())
                .neckY(this.getNeckY())
                .neckToNose(this.getNeckToNose())
                .standardDist(this.getStandardDist())
                .inCreatedAt(this.getInCreatedAt())
                .inUpdatedAt(this.getInUpdatedAt())
                .userId(this.getUserId())
                .name(this.getName())
                .password(this.getPassword())
                .neckToNoseStandard(this.getNeckToNoseStandard())
                .neckAngleOffset(this.getNeckAngleOffset())
                .exId(this.getExId())
                .neckAngle(this.getNeckAngle())
                .torsoAngle(this.getTorsoAngle())
                .exCreatedAt(this.getExCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .isSample(this.getIsSample())
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

    public PostureEntity cloneWithoutId() {
        return new PostureEntity()
                .setInId(this.getInId())
                .setFileName(this.getFileName())
                .setSetNum(this.getSetNum())
                .setOrientationAlpha(this.getOrientationAlpha())
                .setOrientationBeta(this.getOrientationBeta())
                .setOrientationGamma(this.getOrientationGamma())
                .setPitch(this.getPitch())
                .setYaw(this.getYaw())
                .setRoll(this.getRoll())
                .setNoseX(this.getNoseX())
                .setNoseY(this.getNoseY())
                .setNeckX(this.getNeckX())
                .setNeckY(this.getNeckY())
                .setNeckToNose(this.getNeckToNose())
                .setStandardDist(this.getStandardDist())
                .setInCreatedAt(this.getInCreatedAt())
                .setInUpdatedAt(this.getInUpdatedAt())
                .setUserId(this.getUserId())
                .setName(this.getName())
                .setPassword(this.getPassword())
                .setNeckToNoseStandard(this.getNeckToNoseStandard())
                .setNeckAngleOffset(this.getNeckAngleOffset())
                .setExId(this.getExId())
                .setExCreatedAt(this.getExCreatedAt())
                .setUpdatedAt(this.getUpdatedAt())
                .setNeckAngle(this.getNeckAngle())
                .setTorsoAngle(this.getTorsoAngle())
                .setIsSample(this.getIsSample())
                .setTragusX(this.getTragusX())
                .setTragusY(this.getTragusY())
                .setShoulderX(this.getShoulderX())
                .setShoulderY(this.getShoulderY())
                .setWaistX(this.getWaistX())
                .setWaistY(this.getWaistY())
                .setImageWidth(this.getImageWidth())
                .setImageHeight(this.getImageHeight());
    }

    public PostureWithAnnotations toPostureWithAnnotations() {
        Posture posture = this.toPosture();
        List<Annotation> annotations = AnnotationEntity.toAnnotations(this.getAnnotations());
        return new PostureWithAnnotations()
                .posture(posture)
                .annotations(annotations);
    }

}
