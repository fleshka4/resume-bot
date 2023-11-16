package com.resume.bot.model.entity;

import com.resume.bot.model.ResumeData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resumes")
@NoArgsConstructor
@Getter
@Setter
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resumeId;

    // private ResumeData resumeData;

    private String pdfPath;

    private boolean linked;

    @ManyToOne
    private Template template;
}
