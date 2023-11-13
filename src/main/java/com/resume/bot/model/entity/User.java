package com.resume.bot.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private int tgUid;

    @OneToOne(mappedBy = "user")
    private TokenHolder tokenHolder;

    @OneToMany
    @JoinColumn(name = "userId")
    private List<Resume> resumes;
}
