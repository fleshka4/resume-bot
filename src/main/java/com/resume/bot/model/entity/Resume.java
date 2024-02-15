package com.resume.bot.model.entity;

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

    private String resumeData;

    private String pdfPath;

    private String link;

    private String downloadLink;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "tg_uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
}
