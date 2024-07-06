package com.example.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.api.entities.AnnotationEntity;

import jakarta.transaction.Transactional;

@Repository
public interface AnnotationRepository extends JpaRepository<AnnotationEntity, Long> {

    public boolean existsByPostureIdAndAnnotaterId(Long postureId, Long annotaterId);

    public List<AnnotationEntity> findByPostureId(Long postureId);

    public List<AnnotationEntity> findByAnnotaterId(Long annotaterId);

    public AnnotationEntity findByPostureIdAndAnnotaterId(Long postureId, Long annotaterId);

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE annotations SET tragus_x = :tragusX, tragus_y = :tragusY, shoulder_x = :shoulderX, shoulder_y = :shoulderY, waist_x = :waistX, waist_y = :waistY, neck_angle = :neckAngle, torso_angle = :torsoAngle WHERE id = :id",
        nativeQuery = true
    )
    public int updateAnnotationById(
        @Param("id") Long id,
        @Param("tragusX") Double tragusX,
        @Param("tragusY") Double tragusY,
        @Param("shoulderX") Double shoulderX,
        @Param("shoulderY") Double shoulderY,
        @Param("waistX") Double waistX,
        @Param("waistY") Double waistY,
        @Param("neckAngle") Double neckAngle,
        @Param("torsoAngle") Double torsoAngle
    );

    @Transactional
    @Query(
        value = "SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a INNER JOIN postures as p ON a.posture_id = p.id WHERE a.id = :id",
        nativeQuery = true
    )
    public AnnotationEntity findByIdWithFilePath(@Param("id") Long id);

    @Transactional
    @Query(
        value = "SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a INNER JOIN postures as p ON a.posture_id = p.id WHERE a.annotater_id = :annotaterId",
        nativeQuery = true
    )
    public List<AnnotationEntity> findByAnnotaterIdWithFilePath(@Param("annotaterId") Long annotaterId);

    @Transactional
    @Query(
        value = "SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a INNER JOIN postures as p ON a.posture_id = p.id WHERE a.posture_id = :postureId",
        nativeQuery = true
    )
    public List<AnnotationEntity> findByPostureIdWithFilePath(@Param("postureId") Long postureId);

    @Transactional
    @Query(
        value = "SELECT a.*, p.user_id, p.ex_created_at FROM annotations as a INNER JOIN postures as p ON a.posture_id = p.id WHERE a.posture_id = :postureId AND a.annotater_id = :annotaterId",
        nativeQuery = true
    )
    public AnnotationEntity findByPostureIdAndAnnotaterIdWithFilePath(
        @Param("postureId") Long postureId,
        @Param("annotaterId") Long annotaterId
    );

}