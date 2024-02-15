package com.resume.bot.repository;

import com.resume.bot.model.entity.Resume;
import com.resume.bot.model.entity.Template;
import com.resume.bot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    List<Resume> findByUser(User user);

    List<Resume> findByUser_TgUid(Long tgUid);

    Optional<Resume> findByTitle(String title);

    @Transactional
    @Modifying
    @Query("update Resume r set r.link = ?1 where r.resumeId = ?2")
    void updateHhLinkByResumeId(String hhLink, int resumeId);

    @Transactional
    @Modifying
    @Query("update Resume r set r.template = ?1 where r.resumeId = ?2")
    void updateTemplateByResumeId(Template template, int resumeId);

    @Transactional
    @Modifying
    @Query("update Resume r set r.resumeData = ?1 where r.resumeId = ?2")
    void updateResumeDataByResumeId(String resumeData, int resumeId);

    @Transactional
    @Modifying
    @Query("update Resume r set r.pdfPath = ?1 where r.resumeId = ?2")
    int updatePdfPathByResumeId(String pdfPath, int resumeId);
}
