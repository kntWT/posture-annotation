package com.example.api.repositories;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

import com.example.api.entities.AnnotationEntity;
import com.example.api.entities.AnnotationSummaryEntity;

@Repository
public interface AnnotationRepository extends JpaRepository<AnnotationEntity, Long> {

    public List<AnnotationEntity> findAllByOrderByIdAsc();

    public List<AnnotationEntity> findAllByIdInOrderByIdAsc(Iterable<Long> ids);

    public boolean existsByPostureIdAndAnnotaterId(Long postureId, Long annotaterId);

    public List<AnnotationEntity> findByPostureIdOrderByIdAsc(Long postureId);

    public List<AnnotationEntity> findByAnnotaterIdOrderByIdAsc(Long annotaterId);

    public AnnotationEntity findByPostureIdAndAnnotaterId(Long postureId, Long annotaterId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE annotations SET
            tragus_x = :tragusX,
            tragus_y = :tragusY,
            shoulder_x = :shoulderX,
            shoulder_y = :shoulderY,
            waist_x = :waistX,
            waist_y = :waistY,
            neck_angle = :neckAngle,
            torso_angle = :torsoAngle
            WHERE id = :id
            """, nativeQuery = true)
    public int updateAnnotationById(
            @Param("id") Long id,
            @Param("tragusX") Double tragusX,
            @Param("tragusY") Double tragusY,
            @Param("shoulderX") Double shoulderX,
            @Param("shoulderY") Double shoulderY,
            @Param("waistX") Double waistX,
            @Param("waistY") Double waistY,
            @Param("neckAngle") Double neckAngle,
            @Param("torsoAngle") Double torsoAngle);

    @Transactional
    @Query(value = """
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p ON a.posture_id = p.id
            WHERE a.id = :id
            """, nativeQuery = true)
    public AnnotationEntity findByIdWithFilePath(@Param("id") Long id);

    @Transactional
    @Query(value = """
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p ON a.posture_id = p.id
            WHERE a.id in :ids
            ORDer BY a.id ASC
            """, nativeQuery = true)
    public List<AnnotationEntity> findAllByIdWithFilePathOrderByIdAsc(@Param("ids") List<Long> ids);

    @Transactional
    @Query(value = """
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p ON a.posture_id = p.id
            WHERE a.annotater_id = :annotaterId
            """, nativeQuery = true)
    public List<AnnotationEntity> findByAnnotaterIdWithFilePath(@Param("annotaterId") Long annotaterId);

    @Transactional
    public Long countByAnnotaterId(Long annotaterId);

    @Transactional
    @Query(value = """
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p ON a.posture_id = p.id
            WHERE a.posture_id = :postureId
            """, nativeQuery = true)
    public List<AnnotationEntity> findByPostureIdWithFilePath(@Param("postureId") Long postureId);

    @Transactional
    @Query(value = """
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p ON a.posture_id = p.id
            WHERE a.posture_id = :postureId AND a.annotater_id = :annotaterId
            """, nativeQuery = true)
    public AnnotationEntity findByPostureIdAndAnnotaterIdWithFilePath(
            @Param("postureId") Long postureId,
            @Param("annotaterId") Long annotaterId);

    @Transactional
    @Query(value = """
            SELECT a.* FROM annotations as a
            WHERE a.annotater_id = :annotaterId AND a.posture_id NOT IN
                (SELECT p.id FROM postures as p WHERE p.is_sample = TRUE)
            ORDER BY a.id ASC
            """, nativeQuery = true)
    public List<AnnotationEntity> findProdByAnnotaterId(@Param("annotaterId") Long annotaterId);

    @Transactional
    @Query(value = """
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p on a.posture_id = p.id
            WHERE a.annotater_id = :annotaterId AND a.posture_id NOT IN
                (SELECT p.id FROM postures as p WHERE p.is_sample = TRUE)
            ORDER BY a.id ASC
            """, nativeQuery = true)
    public List<AnnotationEntity> findProdByAnnotaterIdWithFilePath(@Param("annotaterId") Long annotaterId);

    @Transactional
    @Query(value = """
            SELECT COUNT(1) FROM annotations as a
            WHERE a.annotater_id = :annotaterId AND a.posture_id IN
                (SELECT p.id FROM postures as p WHERE p.is_sample = :isSample)
            """, nativeQuery = true)
    public Long countByAnnotaterIdAndIsSample(
            @Param("annotaterId") Long annotaterId,
            @Param("isSample") boolean isSample);

    @Transactional
    @Query(value = """
            SELECT a.* FROM annotations as a
            WHERE a.annotater_id = :annotaterId AND a.posture_id IN
                (SELECT p.id FROM postures as p WHERE p.is_sample = TRUE)
            ORDER BY a.id ASC
            """, nativeQuery = true)
    public List<AnnotationEntity> findSampleByAnnotaterId(@Param("annotaterId") Long annotaterId);

    @Transactional
    @Query(value = """
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p on a.posture_id = p.id
            WHERE a.annotater_id = :annotaterId AND a.posture_id IN
                (SELECT p.id FROM postures as p WHERE p.is_sample = TRUE)
            ORDER BY a.id ASC
            """, nativeQuery = true)
    public List<AnnotationEntity> findSampleByAnnotaterIdWithFilePath(@Param("annotaterId") Long annotaterId);

    @Transactional
    @Query(value = """
            SELECT p.is_sample FROM annotations as a
            INNER JOIN postures as p on a.posture_id = p.id
            WHERE a.id = :id
            """, nativeQuery = true)
    public boolean isSampleById(@Param("id") Long id);

    @Transactional
    @Query(value = """
            SELECT p.is_sample FROM annotations as a
            INNER JOIN postures as p on a.posture_id = p.id
            WHERE a.annotater_id = :annotaterId AND a.posture_id = :postureId
            """, nativeQuery = true)
    public boolean isSampleByPostureIdAndAnnotaterId(
            @Param("annotaterId") Long annotaterId,
            @Param("postureId") Long postureId);

    @Transactional
    @Query(value = """
            SELECT
                a.posture_id,
                STRING_AGG(CAST(a.id AS TEXT), ',') AS annotation_ids,
                STRING_AGG(CAST(a.annotater_id AS TEXT), ',') AS annotater_ids,
                MAX(p.neck_angle) AS original_neck_angle,
                AVG(a.neck_angle) AS avg_neck_angle,
                COALESCE(STDDEV(a.neck_angle), 0) AS std_neck_angle,
                MAX(p.user_id) AS user_id,
                MAX(p.ex_created_at) AS ex_created_at
            FROM annotations AS a
            INNER JOIN postures AS p ON a.posture_id = p.id
            GROUP BY a.posture_id
            """, nativeQuery = true)
    public List<Object[]> findAnnotationSummary();

    default public List<AnnotationSummaryEntity> getAnnotationSummary() {
        return findAnnotationSummary().stream()
                .map(row -> new AnnotationSummaryEntity(
                        ((Integer) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        ((Float) row[3]).doubleValue(),
                        (Double) row[4],
                        (Double) row[5],
                        ((Integer) row[6]).longValue(),
                        ((Timestamp) row[7]).toInstant().atOffset(ZoneOffset.ofHours(9))))
                .toList();
    }

}
