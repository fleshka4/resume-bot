package com.resume.bot.json.entity;

import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Data
public class Skills {
    @NonNull
    private List<Type> items;
}
