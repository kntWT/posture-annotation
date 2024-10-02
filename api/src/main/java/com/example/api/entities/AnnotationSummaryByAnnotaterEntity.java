package com.example.api.entities;

import java.util.List;

import org.springframework.data.domain.Page;

import com.generated.model.AnnotationSummaryByAnnotater;
import com.generated.model.AnnotationSummaryByAnnotaterWithPageInfo;

import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.NamedNativeQuery;

@SqlResultSetMapping(name = "AnnotationSummaryByAnnotaterMapping", classes = {
        @ConstructorResult(targetClass = AnnotationSummaryByAnnotaterEntity.class, columns = {
                @ColumnResult(name = "annotater_id", type = Long.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "count", type = Long.class),
                @ColumnResult(name = "avg_diff_original_neck_angle", type = Double.class),
                @ColumnResult(name = "avg_diff_avg_neck_angle", type = Double.class),
                @ColumnResult(name = "std_diff_avg_neck_angle", type = Double.class)
        })
})
// @NamedNativeQuery(name = "AnnotationEntity.findAnnotationSummaryByAnnotater",
// query = , resultSetMapping = "AnnotationSummaryByAnnotaterMapping")
public class AnnotationSummaryByAnnotaterEntity {

    private Long annotaterId;
    private String name;
    private Long count;
    private Double avgDiffOriginalNeckAngle;
    private Double avgDiffAvgNeckAngle;
    private Double stdDiffAvgNeckAngle;

    public AnnotationSummaryByAnnotaterEntity() {
    }

    public AnnotationSummaryByAnnotaterEntity(Long annotaterId, String name, Long count,
            Double avgDiffOriginalNeckAngle,
            Double avgDiffAvgNeckAngle, Double stdDiffAvgNeckAngle) {
        this.annotaterId = annotaterId;
        this.name = name;
        this.count = count;
        this.avgDiffOriginalNeckAngle = avgDiffOriginalNeckAngle;
        this.avgDiffAvgNeckAngle = avgDiffAvgNeckAngle;
        this.stdDiffAvgNeckAngle = stdDiffAvgNeckAngle;
    }

    public Long getAnnotaterId() {
        return annotaterId;
    }

    public AnnotationSummaryByAnnotaterEntity setAnnotaterId(Long annotaterId) {
        this.annotaterId = annotaterId;
        return this;
    }

    public String getName() {
        return name;
    }

    public AnnotationSummaryByAnnotaterEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public AnnotationSummaryByAnnotaterEntity setCount(Long count) {
        this.count = count;
        return this;
    }

    public Double getAvgDiffOriginalNeckAngle() {
        return avgDiffOriginalNeckAngle;
    }

    public AnnotationSummaryByAnnotaterEntity setAvgDiffOriginalNeckAngle(Double avgDiffOriginalNeckAngle) {
        this.avgDiffOriginalNeckAngle = avgDiffOriginalNeckAngle;
        return this;
    }

    public Double getAvgDiffAvgNeckAngle() {
        return avgDiffAvgNeckAngle;
    }

    public AnnotationSummaryByAnnotaterEntity setAvgDiffAvgNeckAngle(Double avgDiffAvgNeckAngle) {
        this.avgDiffAvgNeckAngle = avgDiffAvgNeckAngle;
        return this;
    }

    public Double getStdDiffAvgNeckAngle() {
        return stdDiffAvgNeckAngle;
    }

    public AnnotationSummaryByAnnotaterEntity setStdDiffAvgNeckAngle(Double stdDiffAvgNeckAngle) {
        this.stdDiffAvgNeckAngle = stdDiffAvgNeckAngle;
        return this;
    }

    public static AnnotationSummaryByAnnotater toAnnotationSummaryByAnnotater(
            AnnotationSummaryByAnnotaterEntity entity) {
        return new AnnotationSummaryByAnnotater()
                .annotaterId(entity.getAnnotaterId())
                .name(entity.getName())
                .count(entity.getCount())
                .avgDiffOriginalNeckAngle(entity.getAvgDiffOriginalNeckAngle())
                .avgDiffAvgNeckAngle(entity.getAvgDiffAvgNeckAngle())
                .stdDiffAvgNeckAngle(entity.getStdDiffAvgNeckAngle());
    }

    public static List<AnnotationSummaryByAnnotater> toAnnotationSummaryByAnnotaters(
            List<AnnotationSummaryByAnnotaterEntity> entities) {
        return entities.stream()
                .map(AnnotationSummaryByAnnotaterEntity::toAnnotationSummaryByAnnotater)
                .toList();
    }

    public static AnnotationSummaryByAnnotaterWithPageInfo toAnnotationSummaryByAnnotaterWithPageInfo(
            Page<AnnotationSummaryByAnnotaterEntity> entities) {
        return new AnnotationSummaryByAnnotaterWithPageInfo()
                .contents(
                        AnnotationSummaryByAnnotaterEntity.toAnnotationSummaryByAnnotaters(entities.getContent()))
                .pageNumber((long) entities.getNumber())
                .size((long) entities.getSize())
                .totalPages((long) entities.getTotalPages())
                .isFirst((boolean) entities.isFirst())
                .isLast((boolean) entities.isLast());
    }

    public static AnnotationSummaryByAnnotaterWithPageInfo toAnnotationSummaryByAnnotaterWithPageInfo(
            List<AnnotationSummaryByAnnotaterEntity> entities, Long pageNumber, Long pageSize, Long totalPages,
            Boolean isFirst, Boolean isLast) {
        return new AnnotationSummaryByAnnotaterWithPageInfo()
                .contents(
                        AnnotationSummaryByAnnotaterEntity.toAnnotationSummaryByAnnotaters(entities))
                .pageNumber(pageNumber)
                .size(pageSize)
                .totalPages(totalPages)
                .isFirst(isFirst)
                .isLast(isLast);
    }
}
