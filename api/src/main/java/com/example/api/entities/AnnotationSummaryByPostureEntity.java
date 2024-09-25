package com.example.api.entities;

import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;

import com.example.api.utils.DateFormatter;
import com.generated.model.AnnotationSummaryByPosture;

@SqlResultSetMapping(name = "AnnotationSummaryByPostureMapping", classes = @ConstructorResult(targetClass = AnnotationSummaryByPostureEntity.class, columns = {
        @ColumnResult(name = "posture_id", type = Long.class),
        @ColumnResult(name = "annotation_ids", type = Long[].class),
        @ColumnResult(name = "annotater_ids", type = Long[].class),
        @ColumnResult(name = "original_neck_angle", type = Double.class),
        @ColumnResult(name = "avg_neck_angle", type = Double.class),
        @ColumnResult(name = "std_neck_angle", type = Double.class),
        @ColumnResult(name = "user_id", type = Long.class),
        @ColumnResult(name = "ex_created_at", type = OffsetDateTime.class)
}))
@NamedNativeQuery(name = "AnnotationSummaryByPostureEntity.findAnnotationSummaryByPosture", query = """
        SELECT
            posture_id,
            annotation_ids,
            annotater_ids,
            original_neck_angle,
            avg_neck_angle,
            std_neck_angle,
            user_id,
            ex_created_at
        FROM annotation_summaries
        WHERE user_id = :userId
        ORDER BY ex_created_at DESC
        LIMIT :limit
        OFFSET :offset
        """, resultSetMapping = "AnnotationSummaryByPostureMapping")
public class AnnotationSummaryByPostureEntity {

    private Long postureId;
    private String annotationIds;
    private String annotaterIds;

    private Double originalNeckAngle;

    private Double avgNeckAngle;

    private Double stdNeckAngle;

    private Long userId;

    private OffsetDateTime exCreatedAt;

    public AnnotationSummaryByPostureEntity() {
    }

    public AnnotationSummaryByPostureEntity(Long postureId, String annotationIds, String annotaterIds,
            Double originalNeckAngle, Double avgNeckAngle, Double stdNeckAngle, Long userId,
            OffsetDateTime exCreatedAt) {
        this.postureId = postureId;
        this.annotationIds = annotationIds;
        this.annotaterIds = annotaterIds;
        this.originalNeckAngle = originalNeckAngle;
        this.avgNeckAngle = avgNeckAngle;
        this.stdNeckAngle = stdNeckAngle;
        this.userId = userId;
        this.exCreatedAt = exCreatedAt;
    }

    public Long getPostureId() {
        return postureId;
    }

    public AnnotationSummaryByPostureEntity setPostureId(Long postureId) {
        this.postureId = postureId;
        return this;
    }

    public String getAnnotationIds() {
        return annotationIds;
    }

    public AnnotationSummaryByPostureEntity setAnnotationIds(String annotationIds) {
        this.annotationIds = annotationIds;
        return this;
    }

    public String getAnnotaterIds() {
        return annotaterIds;
    }

    public AnnotationSummaryByPostureEntity setAnnotaterIds(String annotaterIds) {
        this.annotaterIds = annotaterIds;
        return this;
    }

    public Double getOriginalNeckAngle() {
        return originalNeckAngle;
    }

    public AnnotationSummaryByPostureEntity setOriginalNeckAngle(Double originalNeckAngle) {
        this.originalNeckAngle = originalNeckAngle;
        return this;
    }

    public Double getAvgNeckAngle() {
        return avgNeckAngle;
    }

    public AnnotationSummaryByPostureEntity setAvgNeckAngle(Double avgNeckAngle) {
        this.avgNeckAngle = avgNeckAngle;
        return this;
    }

    public Double getStdNeckAngle() {
        return stdNeckAngle;
    }

    public AnnotationSummaryByPostureEntity setStdNeckAngle(Double stdNeckAngle) {
        this.stdNeckAngle = stdNeckAngle;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public AnnotationSummaryByPostureEntity setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public OffsetDateTime getExCreatedAt() {
        return exCreatedAt;
    }

    public AnnotationSummaryByPostureEntity setExCreatedAt(OffsetDateTime exCreatedAt) {
        this.exCreatedAt = exCreatedAt;
        return this;
    }

    private static List<Long> splitStringToLongList(String str) {
        return List.of(str.split(",")).stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public String toString() {
        return "AnnotationSummaryByPostureEntity{" +
                "postureId=" + postureId +
                ", annotationIds=" + annotationIds +
                ", annotaterIds=" + annotaterIds +
                ", originalNeckAngle=" + originalNeckAngle +
                ", avgNeckAngle=" + avgNeckAngle +
                ", stdNeckAngle=" + stdNeckAngle +
                ", userId=" + userId +
                ", exCreatedAt=" + exCreatedAt +
                '}';
    }

    public static AnnotationSummaryByPosture toAnnotationSummaryByPosture(
            AnnotationSummaryByPostureEntity annotationSummaryEntity) {
        String fileName = annotationSummaryEntity.getUserId() + "/"
                + DateFormatter.format(annotationSummaryEntity.getExCreatedAt()) + ".jpg";
        return new AnnotationSummaryByPosture()
                .postureId(annotationSummaryEntity.getPostureId())
                .annotationIds(splitStringToLongList(annotationSummaryEntity.getAnnotationIds()))
                .annotaterIds(splitStringToLongList(annotationSummaryEntity.getAnnotaterIds()))
                .originalNeckAngle(annotationSummaryEntity.getOriginalNeckAngle())
                .avgNeckAngle(annotationSummaryEntity.getAvgNeckAngle())
                .stdNeckAngle(annotationSummaryEntity.getStdNeckAngle())
                .fileName(fileName);
    }

    public static List<AnnotationSummaryByPosture> toAnnotationSummaries(
            List<AnnotationSummaryByPostureEntity> annotationSummaryEntities) {
        return annotationSummaryEntities.stream()
                .map(AnnotationSummaryByPostureEntity::toAnnotationSummaryByPosture)
                .toList();
    }

}
