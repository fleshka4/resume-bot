package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Relocation {
    @NonNull List<Area> area;
    @NonNull List<Type> district;
    @NonNull Type type;
}
