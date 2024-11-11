package com.example.api.entities;

import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Page;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;

import com.example.api.utils.DateFormatter;
import com.generated.model.AnnotationSummaryByPosture;
import com.generated.model.AnnotationSummaryByPostureWithPageInfo;

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
    private Boolean isSample;
    private String annotationIds;
    private String annotaterIds;

    private Double originalNeckAngle;

    private Double avgNeckAngle;

    private Double maxDiffNeckAngle;

    private Long userId;

    private OffsetDateTime exCreatedAt;

    public AnnotationSummaryByPostureEntity() {
    }

    public AnnotationSummaryByPostureEntity(Long postureId, Boolean isSample, String annotationIds, String annotaterIds,
            Double originalNeckAngle, Double avgNeckAngle, Double maxDiffNeckAngle, Long userId,
            OffsetDateTime exCreatedAt) {
        this.postureId = postureId;
        this.isSample = isSample;
        this.annotationIds = annotationIds;
        this.annotaterIds = annotaterIds;
        this.originalNeckAngle = originalNeckAngle;
        this.avgNeckAngle = avgNeckAngle;
        this.maxDiffNeckAngle = maxDiffNeckAngle;
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

    public Boolean getIsSample() {
        return isSample;
    }

    public AnnotationSummaryByPostureEntity setIsSample(Boolean isSample) {
        this.isSample = isSample;
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

    public Double getMaxDiffNeckAngle() {
        return maxDiffNeckAngle;
    }

    public AnnotationSummaryByPostureEntity setMaxDiffNeckAngle(Double maxDiffNeckAngle) {
        this.maxDiffNeckAngle = maxDiffNeckAngle;
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
                ", isSample=" + isSample +
                ", annotationIds=" + annotationIds +
                ", annotaterIds=" + annotaterIds +
                ", originalNeckAngle=" + originalNeckAngle +
                ", avgNeckAngle=" + avgNeckAngle +
                ", maxDiffNeckAngle=" + maxDiffNeckAngle +
                ", userId=" + userId +
                ", exCreatedAt=" + exCreatedAt +
                '}';
    }

    public static AnnotationSummaryByPosture toAnnotationSummaryByPosture(
            AnnotationSummaryByPostureEntity entity) {
        String fileName = entity.getUserId() + "/"
                + DateFormatter.format(entity.getExCreatedAt()) + ".jpg";
        return new AnnotationSummaryByPosture()
                .postureId(entity.getPostureId())
                .isSample(entity.getIsSample())
                .annotationIds(splitStringToLongList(entity.getAnnotationIds()))
                .annotaterIds(splitStringToLongList(entity.getAnnotaterIds()))
                .originalNeckAngle(entity.getOriginalNeckAngle())
                .avgNeckAngle(entity.getAvgNeckAngle())
                .maxDiffNeckAngle(entity.getMaxDiffNeckAngle())
                .fileName(fileName);
    }

    public static List<AnnotationSummaryByPosture> toAnnotationSummaryByPostures(
            List<AnnotationSummaryByPostureEntity> entities) {
        return entities.stream()
                .map(AnnotationSummaryByPostureEntity::toAnnotationSummaryByPosture)
                .toList();
    }

    public static AnnotationSummaryByPostureWithPageInfo toAnnotationSummaryByPostureWithPageInfo(
            Page<AnnotationSummaryByPostureEntity> entity) {
        return new AnnotationSummaryByPostureWithPageInfo()
                .contents(
                        AnnotationSummaryByPostureEntity.toAnnotationSummaryByPostures(entity.getContent()))
                .pageNumber((long) entity.getNumber())
                .size((long) entity.getSize())
                .totalPages((long) entity.getTotalPages())
                .isFirst((boolean) entity.isFirst())
                .isLast((boolean) entity.isLast());
    }

    public static AnnotationSummaryByPostureWithPageInfo toAnnotationSummaryByPostureWithPageInfo(
            List<AnnotationSummaryByPostureEntity> entities, Long pageNumber, Long pageSize, Long totalPages,
            Boolean isFirst, Boolean isLast) {
        return new AnnotationSummaryByPostureWithPageInfo()
                .contents(AnnotationSummaryByPostureEntity.toAnnotationSummaryByPostures(entities))
                .pageNumber(pageNumber)
                .size(pageSize)
                .totalPages(totalPages)
                .isFirst(isFirst)
                .isLast(isLast);
    }

}
