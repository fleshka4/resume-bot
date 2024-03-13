package com.resume.util;

import com.resume.bot.json.entity.area.Area;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;

import static com.resume.util.Constants.OTHER_COUNTRIES_JSON_ID;

@UtilityClass
public class ConstantsUtil {
    public static Optional<Area> getAreaByNameDeep(List<Area> areas, String name) {
        for (Area area : areas) {
            if (area.getName().equals(name)) {
                return Optional.of(area);
            } else if (!area.getAreas().isEmpty()) {
                Optional<Area> childArea = getAreaByNameDeep(area.getAreas(), name);
                if (childArea.isPresent()) {
                    return childArea;
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<Area> getAreaByIdDeep(List<Area> areas, String id) {
        for (Area area : areas) {
            if (area.getId().equals(id)) {
                return Optional.of(area);
            } else if (!area.getAreas().isEmpty()) {
                Optional<Area> childArea = getAreaByIdDeep(area.getAreas(), id);
                if (childArea.isPresent()) {
                    return childArea;
                }
            }
        }
        return Optional.empty();
    }

    public static String getAreaString(List<Area> areas, Area area) {
        StringBuilder areaSB = new StringBuilder();
        while (area != null) {
            if (!area.getId().equals(OTHER_COUNTRIES_JSON_ID)) {
                areaSB.insert(0, area.getName()).insert(0, area.getParentId() != null ? ", " : "");
            }
            area = getAreaByIdDeep(areas, area.getParentId()).orElse(null);
        }
        return areaSB.toString();
    }
}
