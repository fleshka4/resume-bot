package com.resume.bot.json.entity.client;

import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Data
public class Relocation {
    @NonNull
    private List<Area> area;

    @NonNull
    private List<Type> district;

    @NonNull
    private Type type;
}
