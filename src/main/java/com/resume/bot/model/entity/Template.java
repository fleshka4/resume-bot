package com.resume.bot.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "templates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Template {

    @Id
    @Column(name = "template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int templateId;

    private String imagePath;

    private String sourcePath;
}
