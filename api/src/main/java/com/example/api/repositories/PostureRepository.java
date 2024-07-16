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

    public List<PostureEntity> findByAnnotaterId(Long annotaterId);

    public List<PostureEntity> findByUserId(Long userId);

    public List<PostureEntity> findByAnnotaterIdIsNull();

    @Query(value = "SELECT p.* FROM postures AS p WHERE p.annotater_id IS NULL ORDER BY random() LIMIT :limit", nativeQuery = true)
    public List<PostureEntity> findByAnnotaterIdIsNullLimitedTo(@Param("limit") int limit);

    @Query(
        value = "SELECT p.* FROM postures AS p left join (select a.posture_id, coalesce(count(*), 0) as count from annotations as a group by a.posture_id) as counts on p.id = counts.posture_id ORDER BY counts.count desc, random() LIMIT :limit",
        nativeQuery = true
    )
    public List<PostureEntity> findOrderByAnnotationCountLimitedTo(@Param("limit") int limit);

    @Query(
        value = "SELECT p.* FROM postures AS p left join (select a.posture_id, coalesce(count(*), 0) as count from annotations as a group by a.posture_id) as counts on p.id = counts.posture_id WHERE annotater_id = 0 ORDER BY counts.count desc, random() LIMIT :limit",
        nativeQuery = true
    )
    public List<PostureEntity> findSampleDataByOrderByAnnotationCountLimitedTo(@Param("limit") int limit);

    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT p.* FROM postures AS p WHERE p.id = :id FOR UPDATE", nativeQuery = true)
    @QueryHints({
        @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3600000"),
        @QueryHint(name = "jakarta.persistence.query.timeout", value = "1000")
    })
    public PostureEntity findByIdWithLock(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE postures SET neck_angle = :neckAngle, torso_angle = :torsoAngle, annotater_id = :annotaterId WHERE id = :id",
        nativeQuery = true
    )
    public int updatePostureById(
        @Param("id") Long id,
        @Param("neckAngle") Double neckAngle,
        @Param("torsoAngle") Double torsoAngle,
        @Param("annotaterId") Long annotaterId
    );

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE postures SET tragus_x = :tragusX, tragus_y = :tragusY, shoulder_x = :shoulderX, shoulder_y = :shoulderY, waist_x = :waistX, waist_y = :waistY, image_width = :imageWidth, image_height = :imageHeight WHERE id = :id",
        nativeQuery = true
    )
    public int updatePostureById(
        @Param("id") Long id,
        @Param("tragusX") Double tragusX,
        @Param("tragusY") Double tragusY,
        @Param("shoulderX") Double shoulderX,
        @Param("shoulderY") Double shoulderY,
        @Param("waistX") Double waistX,
        @Param("waistY") Double waistY,
        @Param("imageWidth") Double imageWidth,
        @Param("imageHeight") Double imageHeight
    );

    @Transactional
    @Query(
        value = "SELECT p.id FROM postures AS p ORDER BY p.id DESC LIMIT 1",
        nativeQuery = true
    )
    public Long findLastId();

    @Transactional
    @Query(
        value = "SELECT p.* FROM postures AS p WHERE p.annotater_id IS NULL ORDER BY random() LIMIT :limit",
        nativeQuery = true
    )
    public List<PostureEntity> findByOrderByRandomLimitTo(@Param("limit") Long limit);

}
