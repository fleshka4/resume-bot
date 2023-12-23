package com.resume.bot.json.entity.client;

import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
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
