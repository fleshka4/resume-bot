package com.resume.bot.repository;

import com.resume.bot.model.entity.Resume;
import com.resume.bot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    List<Resume> findByUser(User user);

    List<Resume> findByUser_TgUid(Long tgUid);

    @Transactional
    @Modifying
    @Query("update Resume r set r.hhLink = ?1 where r.resumeId = ?2")
    int updateHhLinkByResumeId(String hhLink, int resumeId);
}
