package com.unit.resume.util;

import com.resume.bot.json.entity.area.Area;
import com.resume.util.ConstantsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConstantsUtilTest {
    private static final List<Area> areas = new ArrayList<>();
    private static final Area russia = new Area("1", null, "Россия", new ArrayList<>());
    private static final Area moscow = new Area("2", "1", "Москва", new ArrayList<>());
    private static final Area leninradArea = new Area("3", "1", "Ленинградская область", new ArrayList<>());
    private static final Area vsevolozhsk = new Area("4", "3", "Всеволожск", new ArrayList<>());
    private static final Area belarus = new Area("5", null, "Беларусь", new ArrayList<>());

    @BeforeAll
    public static void fillAreas() {
        russia.getAreas().add(moscow);
        russia.getAreas().add(leninradArea);
        leninradArea.getAreas().add(vsevolozhsk);

        areas.add(russia);
        areas.add(belarus);
    }

    @Test
    public void getAreaByNameDeepTest() {
        Assertions.assertEquals(ConstantsUtil.getAreaByNameDeep(areas, "абывбавы"), Optional.empty());
        Assertions.assertEquals(ConstantsUtil.getAreaByNameDeep(areas, "Беларусь").get(), belarus);
        Assertions.assertEquals(ConstantsUtil.getAreaByNameDeep(areas, "Россия").get(), russia);
        Assertions.assertEquals(ConstantsUtil.getAreaByNameDeep(areas, "Москва").get(), moscow);
        Assertions.assertEquals(ConstantsUtil.getAreaByNameDeep(areas, "Ленинградская область").get(), leninradArea);
        Assertions.assertEquals(ConstantsUtil.getAreaByNameDeep(areas, "Всеволожск").get(), vsevolozhsk);
    }

    @Test
    public void getAreaByIdDeepTest() {
        Assertions.assertEquals(ConstantsUtil.getAreaByIdDeep(areas, "312"), Optional.empty());
        Assertions.assertEquals(ConstantsUtil.getAreaByIdDeep(areas, "5").get(), belarus);
        Assertions.assertEquals(ConstantsUtil.getAreaByIdDeep(areas, "1").get(), russia);
        Assertions.assertEquals(ConstantsUtil.getAreaByIdDeep(areas, "2").get(), moscow);
        Assertions.assertEquals(ConstantsUtil.getAreaByIdDeep(areas, "3").get(), leninradArea);
        Assertions.assertEquals(ConstantsUtil.getAreaByIdDeep(areas, "4").get(), vsevolozhsk);
    }

    @Test
    public void getAreaStringTest() {
        Assertions.assertEquals(ConstantsUtil.getAreaString(areas, null), "");
        Assertions.assertEquals(ConstantsUtil.getAreaString(areas, belarus), "Беларусь");
        Assertions.assertEquals(ConstantsUtil.getAreaString(areas, russia), "Россия");
        Assertions.assertEquals(ConstantsUtil.getAreaString(areas, moscow), "Россия, Москва");
        Assertions.assertEquals(ConstantsUtil.getAreaString(areas, leninradArea), "Россия, Ленинградская область");
        Assertions.assertEquals(ConstantsUtil.getAreaString(areas, vsevolozhsk), "Россия, Ленинградская область, Всеволожск");
    }
}
