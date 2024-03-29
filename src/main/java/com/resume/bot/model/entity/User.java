package com.resume.bot.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "tg_uid")
    private Long tgUid;

    @OneToOne(mappedBy = "user")
    private TokenHolder tokenHolder;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Resume> resumes;

    public User(Long id) {
        tgUid = id;
    }
}
