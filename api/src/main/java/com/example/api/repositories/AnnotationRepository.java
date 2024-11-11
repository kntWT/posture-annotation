package com.example.api.repositories;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

import com.example.api.entities.AnnotationEntity;
import com.example.api.entities.AnnotationSummaryByAnnotaterEntity;
import com.example.api.entities.AnnotationSummaryByPostureEntity;
import com.generated.model.AnnotationSummaryByPostureWithPageInfo;
import com.generated.model.AnnotationSummaryByAnnotaterWithPageInfo;

@Repository
public interface AnnotationRepository extends JpaRepository<AnnotationEntity, Long> {

    public List<AnnotationEntity> findAllByOrderByIdAsc();

    public Page<AnnotationEntity> findAllByOrderByIdAsc(Pageable pageable);

    public List<AnnotationEntity> findAllByIdInOrderByIdAsc(Iterable<Long> ids);

    public boolean existsByPostureIdAndAnnotaterId(Long postureId, Long annotaterId);

    public List<AnnotationEntity> findByPostureIdOrderByIdAsc(Long postureId);

    public Page<AnnotationEntity> findByPostureIdOrderByIdAsc(Long postureId, Pageable pageable);

    public List<AnnotationEntity> findByAnnotaterIdOrderByIdAsc(Long annotaterId);

    public Page<AnnotationEntity> findByAnnotaterIdOrderByIdAsc(Long annotaterId, Pageable pageable);

    public AnnotationEntity findByPostureIdAndAnnotaterId(Long postureId, Long annotaterId);

    public List<AnnotationEntity> findAllWithPostureByAnnotaterId(Long annotaterId);

    public Page<AnnotationEntity> findAllWithPostureByAnnotaterId(Long annotaterId, Pageable pageable);

    public AnnotationEntity findWithPostureById(Long id);

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
    @Query(value = """
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p ON a.posture_id = p.id
            WHERE a.annotater_id = :annotaterId
            """, nativeQuery = true)
    public Page<AnnotationEntity> findByAnnotaterIdWithFilePath(@Param("annotaterId") Long annotaterId, Pageable page);

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
            WHERE a.posture_id = :postureId
            """, nativeQuery = true)
    public Page<AnnotationEntity> findByPostureIdWithFilePath(@Param("postureId") Long postureId, Pageable pageable);

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
            SELECT a.* FROM annotations as a
            WHERE a.annotater_id = :annotaterId AND a.posture_id NOT IN
                (SELECT p.id FROM postures as p WHERE p.is_sample = TRUE)
            ORDER BY a.id ASC
            """, nativeQuery = true)
    public Page<AnnotationEntity> findProdByAnnotaterId(@Param("annotaterId") Long annotaterId, Pageable pageable);

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
            SELECT a FROM AnnotationEntity a
            JOIN FETCH a.posture p
            WHERE a.annotaterId = :annotaterId AND p.isSample = false
            """)
    public Page<AnnotationEntity> findProdByAnnotaterIdWithFilePath(@Param("annotaterId") Long annotaterId,
            Pageable pageable);

    @Transactional
    @Query(value = """
            SELECT a FROM AnnotationEntity a
            JOIN FETCH a.posture p
            WHERE a.annotaterId = :annotaterId AND p.isSample = false
            """)
    public List<AnnotationEntity> findProdWithPostureByAnnotaterId(@Param("annotaterId") Long annotaterId);

    @Transactional
    @Query(value = """
            SELECT a FROM AnnotationEntity a
            JOIN FETCH a.posture p
            WHERE a.annotaterId = :annotaterId AND p.isSample = false
            """)
    public Page<AnnotationEntity> findProdWithPostureByAnnotaterId(@Param("annotaterId") Long annotaterId,
            Pageable pageable);

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
            SELECT a.* FROM annotations as a
            WHERE a.annotater_id = :annotaterId AND a.posture_id IN
                (SELECT p.id FROM postures as p WHERE p.is_sample = TRUE)
            ORDER BY a.id ASC
            """, nativeQuery = true)
    public Page<AnnotationEntity> findSampleByAnnotaterId(@Param("annotaterId") Long annotaterId, Pageable pageable);

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
            SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a
            INNER JOIN postures as p on a.posture_id = p.id
            WHERE a.annotater_id = :annotaterId AND a.posture_id IN
                (SELECT p.id FROM postures as p WHERE p.is_sample = TRUE)
            ORDER BY a.id ASC
            """, nativeQuery = true)
    public Page<AnnotationEntity> findSampleByAnnotaterIdWithFilePath(@Param("annotaterId") Long annotaterId,
            Pageable pageable);

    @Transactional
    @Query(value = """
            SELECT a FROM AnnotationEntity a
            JOIN FETCH a.posture p
            WHERE a.annotaterId = :annotaterId AND p.isSample = TRUE
            """)
    public List<AnnotationEntity> findSampleWithPostureByAnnotaterId(@Param("annotaterId") Long annotaterId);

    @Transactional
    @Query(value = """
            SELECT a FROM AnnotationEntity a
            JOIN FETCH a.posture p
            WHERE a.annotaterId = :annotaterId AND p.isSample = TRUE
            """)
    public Page<AnnotationEntity> findSampleWithPostureByAnnotaterId(@Param("annotaterId") Long annotaterId,
            Pageable pageable);

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
                BOOL_AND(p.is_sample) AS is_sample,
                STRING_AGG(CAST(a.id AS TEXT), ',') AS annotation_ids,
                STRING_AGG(CAST(a.annotater_id AS TEXT), ',') AS annotater_ids,
                MAX(p.neck_angle) AS original_neck_angle,
                AVG(a.neck_angle) AS avg_neck_angle,
                MAX(a.neck_angle) - MIN(a.neck_angle) AS max_diff_neck_angle,
                MAX(p.user_id) AS user_id,
                MAX(p.ex_created_at) AS ex_created_at
            FROM annotations AS a
            INNER JOIN postures AS p ON a.posture_id = p.id
            GROUP BY a.posture_id
            """, nativeQuery = true)
    public List<Object[]> findAnnotationSummaryByPosture();

    @Transactional
    @Query(value = """
            SELECT
                a.posture_id,
                BOOL_AND(p.is_sample) AS is_sample,
                STRING_AGG(CAST(a.id AS TEXT), ',') AS annotation_ids,
                STRING_AGG(CAST(a.annotater_id AS TEXT), ',') AS annotater_ids,
                MAX(p.neck_angle) AS original_neck_angle,
                AVG(a.neck_angle) AS avg_neck_angle,
                MAX(a.neck_angle) - MIN(a.neck_angle) AS max_diff_neck_angle,
                MAX(p.user_id) AS user_id,
                MAX(p.ex_created_at) AS ex_created_at
            FROM annotations AS a
            INNER JOIN postures AS p ON a.posture_id = p.id
            GROUP BY a.posture_id
            """, nativeQuery = true)
    public Page<Object[]> findAnnotationSummaryByPosture(Pageable pageable);

    default public List<AnnotationSummaryByPostureEntity> getAnnotationSummaryByPosture() {
        return findAnnotationSummaryByPosture().stream()
                .map(row -> new AnnotationSummaryByPostureEntity(
                        (Long) row[0],
                        (Boolean) row[1],
                        (String) row[2],
                        (String) row[3],
                        ((Float) row[4]).doubleValue(),
                        (Double) row[5],
                        ((Float) row[6]).doubleValue(),
                        (Long) row[7],
                        ((Instant) row[8]).atOffset(ZoneOffset.ofHours(9))))
                .toList();
    }

    default public AnnotationSummaryByPostureWithPageInfo getAnnotationSummaryByPosture(Pageable pageable) {
        Page<Object[]> entityWithPage = findAnnotationSummaryByPosture(pageable);
        List<AnnotationSummaryByPostureEntity> entity = entityWithPage.getContent().stream()
                .map(row -> new AnnotationSummaryByPostureEntity(
                        (Long) row[0],
                        (Boolean) row[1],
                        (String) row[2],
                        (String) row[3],
                        ((Float) row[4]).doubleValue(),
                        (Double) row[5],
                        ((Float) row[6]).doubleValue(),
                        (Long) row[7],
                        ((Instant) row[8]).atOffset(ZoneOffset.ofHours(9))))
                .toList();
        return AnnotationSummaryByPostureEntity.toAnnotationSummaryByPostureWithPageInfo(entity,
                (long) entityWithPage.getNumber(), (long) entityWithPage.getSize(),
                (long) entityWithPage.getTotalPages(), (boolean) entityWithPage.isFirst(),
                (boolean) entityWithPage.isLast());
    }

    @Transactional
    @Query(value = """
            SELECT
                a.annotater_id,
                MAX(u.name) as name,
                COUNT(1) AS count,
                AVG(a.neck_angle - p.neck_angle) AS avg_diff_original_neck_angle,
                AVG(a.neck_angle - avg.neck_angle) AS avg_diff_avg_neck_angle,
                COALESCE(STDDEV(a.neck_angle - avg.neck_angle), 0) AS std_diff_avg_neck_angle
            FROM annotations AS a
            INNER JOIN users AS u ON a.annotater_id = u.id
            INNER JOIN postures AS p ON a.posture_id = p.id
            INNER JOIN (
                SELECT a.posture_id, AVG(a.neck_angle) AS neck_angle
                FROM annotations AS a
                GROUP BY a.posture_id
            ) AS avg ON a.posture_id = avg.posture_id
            GROUP BY a.annotater_id
            """, nativeQuery = true)
    public List<Object[]> findAnnotationSummaryByAnnotater();

    @Transactional
    @Query(value = """
            SELECT
                a.annotater_id,
                MAX(u.name) as name,
                COUNT(1) AS count,
                AVG(a.neck_angle - p.neck_angle) AS avg_diff_original_neck_angle,
                AVG(a.neck_angle - avg.neck_angle) AS avg_diff_avg_neck_angle,
                COALESCE(STDDEV(a.neck_angle - avg.neck_angle), 0) AS std_diff_avg_neck_angle
            FROM annotations AS a
            INNER JOIN users AS u ON a.annotater_id = u.id
            INNER JOIN postures AS p ON a.posture_id = p.id
            INNER JOIN (
                SELECT a.posture_id, AVG(a.neck_angle) AS neck_angle
                FROM annotations AS a
                GROUP BY a.posture_id
            ) AS avg ON a.posture_id = avg.posture_id
            GROUP BY a.annotater_id
            """, nativeQuery = true)
    public Page<Object[]> findAnnotationSummaryByAnnotater(Pageable pageable);

    default public List<AnnotationSummaryByAnnotaterEntity> getAnnotationSummaryByAnnotater() {
        return findAnnotationSummaryByAnnotater().stream()
                .map(row -> new AnnotationSummaryByAnnotaterEntity(
                        (Long) row[0],
                        (String) row[1],
                        ((Long) row[2]),
                        (Double) row[3],
                        (Double) row[4],
                        (Double) row[5]))
                .toList();
    }

    default public AnnotationSummaryByAnnotaterWithPageInfo getAnnotationSummaryByAnnotater(Pageable pageable) {
        Page<Object[]> entityWithPage = findAnnotationSummaryByAnnotater(pageable);
        List<AnnotationSummaryByAnnotaterEntity> entity = entityWithPage.getContent().stream()
                .map(row -> new AnnotationSummaryByAnnotaterEntity(
                        (Long) row[0],
                        (String) row[1],
                        (Long) row[2],
                        (Double) row[3],
                        (Double) row[4],
                        (Double) row[5]))
                .toList();
        return AnnotationSummaryByAnnotaterEntity.toAnnotationSummaryByAnnotaterWithPageInfo(entity,
                (long) entityWithPage.getNumber(), (long) entityWithPage.getSize(),
                (long) entityWithPage.getTotalPages(), (boolean) entityWithPage.isFirst(),
                (boolean) entityWithPage.isLast());
    }

}
