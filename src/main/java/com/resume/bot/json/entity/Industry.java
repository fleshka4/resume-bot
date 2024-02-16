package com.resume.bot.json.entity;

import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Industry {
    @NonNull
    private String id;

    @NonNull
    private List<Type> industries;

    @NonNull
    private String name;
}
