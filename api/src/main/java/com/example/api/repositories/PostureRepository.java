package com.example.api.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

import com.example.api.entities.PostureEntity;
import com.generated.model.PostureUpdate;

@Repository
public interface PostureRepository extends JpaRepository<PostureEntity, Long> {

    public PostureEntity findByInId(Long inId);

    public List<PostureEntity> findByUserId(Long userId);

    public List<PostureEntity> findByIsSample(Boolean isSample);

    @Query(value = "SELECT p.* FROM postures AS p WHERE p.is_sample = FALSE ORDER BY random() LIMIT :limit", nativeQuery = true)
    public List<PostureEntity> findProdByLimitedTo(@Param("limit") int limit);

    @Query(value = "SELECT p.* FROM postures AS p left join (select a.posture_id, coalesce(count(*), 0) as count from annotations as a group by a.posture_id) as counts on p.id = counts.posture_id ORDER BY counts.count desc, random() LIMIT :limit", nativeQuery = true)
    public List<PostureEntity> findOrderByAnnotationCountLimitedTo(@Param("limit") int limit);

    @Transactional
    @Query(value = """
                SELECT p.* FROM postures AS p
                LEFT JOIN (SELECT a.posture_id, COUNT(*) AS count FROM annotations AS a GROUP BY a.posture_id) AS counts ON p.id = counts.posture_id
                WHERE p.id NOT IN (SELECT a.posture_id FROM annotations AS a WHERE a.annotater_id = :annotaterId) AND is_sample = FALSE
                ORDER BY CASE WHEN COALESCE(counts.count, 0) > :threshold THEN -1 ELSE COALESCE(counts.count, 0) END DESC, RANDOM()
                LIMIT :limit
            """, nativeQuery = true)
    public List<PostureEntity> findByAnnotaterIdOrderByAnnotationCountLimitTo(
            @Param("annotaterId") Long annotaterId,
            @Param("limit") int limit,
            @Param("threshold") int threshold);

    @Query(value = "SELECT p.* FROM postures AS p left join (select a.posture_id, coalesce(count(*), 0) as count from annotations as a group by a.posture_id) as counts on p.id = counts.posture_id WHERE is_sample = TRUE ORDER BY counts.count desc, random() LIMIT :limit", nativeQuery = true)
    public List<PostureEntity> findSampleByOrderByAnnotationCountLimitedTo(@Param("limit") int limit);

    @Transactional
    @Query(value = """
                SELECT p.* FROM postures AS p
                LEFT JOIN (SELECT a.posture_id, COUNT(*) AS count FROM annotations AS a GROUP BY a.posture_id) AS counts ON p.id = counts.posture_id
                WHERE p.id NOT IN (SELECT a.posture_id FROM annotations AS a WHERE a.annotater_id = :annotaterId) AND is_sample = TRUE
                ORDER BY CASE WHEN COALESCE(counts.count, 0) > :threshold THEN -1 ELSE COALESCE(counts.count, 0) END DESC, RANDOM()
                LIMIT :limit
            """, nativeQuery = true)
    public List<PostureEntity> findSampleByAnnotaterIdOrderByAnnotationCountLimitTo(
            @Param("annotaterId") Long annotaterId,
            @Param("limit") int limit,
            @Param("threshold") int threshold);

    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT p.* FROM postures AS p WHERE p.id = :id FOR UPDATE", nativeQuery = true)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3600000"),
            @QueryHint(name = "jakarta.persistence.query.timeout", value = "1000")
    })
    public PostureEntity findByIdWithLock(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE postures SET neck_angle = :neckAngle, torso_angle = :torsoAngle WHERE id = :id", nativeQuery = true)
    public int updatePostureById(
            @Param("id") Long id,
            @Param("neckAngle") Double neckAngle,
            @Param("torsoAngle") Double torsoAngle);

    @Modifying
    @Transactional
    @Query(value = "UPDATE postures SET tragus_x = :tragusX, tragus_y = :tragusY, shoulder_x = :shoulderX, shoulder_y = :shoulderY, waist_x = :waistX, waist_y = :waistY, image_width = :imageWidth, image_height = :imageHeight WHERE id = :id", nativeQuery = true)
    public int updatePostureById(
            @Param("id") Long id,
            @Param("tragusX") Double tragusX,
            @Param("tragusY") Double tragusY,
            @Param("shoulderX") Double shoulderX,
            @Param("shoulderY") Double shoulderY,
            @Param("waistX") Double waistX,
            @Param("waistY") Double waistY,
            @Param("imageWidth") Double imageWidth,
            @Param("imageHeight") Double imageHeight);

    @Transactional
    @Query(value = "SELECT p.id FROM postures AS p ORDER BY p.id DESC LIMIT 1", nativeQuery = true)
    public Long findLastId();

    @Transactional
    @Query(value = "SELECT p.* FROM postures AS p WHERE p.is_sample = FALSE ORDER BY random() LIMIT :limit", nativeQuery = true)
    public List<PostureEntity> findByOrderByRandomLimitTo(@Param("limit") Long limit);

}
