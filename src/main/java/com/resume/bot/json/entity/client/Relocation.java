package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public static Relocation createRelocation(
            @JsonProperty("area") @NonNull List<Area> area,
            @JsonProperty("district") @NonNull List<Type> district,
            @JsonProperty("type") @NonNull Type type) {
        return new Relocation(area, district, type);
    }
}
