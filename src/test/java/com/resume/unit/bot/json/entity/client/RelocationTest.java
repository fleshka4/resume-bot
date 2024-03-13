package com.resume.unit.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Area;
import com.resume.bot.json.entity.client.Relocation;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RelocationTest {
    @Test
    public void gettersAndSettersTest() {
        List<Area> areaList = new ArrayList<>();
        List<Type> districtList = new ArrayList<>();
        Type type = new Type("1", "Type");

        Relocation relocation = new Relocation();

        relocation.setArea(areaList);
        relocation.setDistrict(districtList);
        relocation.setType(type);

        assertEquals(areaList, relocation.getArea());
        assertEquals(districtList, relocation.getDistrict());
        assertEquals(type, relocation.getType());
    }

    @Test
    public void constructorsTest() {
        List<Area> areaList = new ArrayList<>();
        List<Type> districtList = new ArrayList<>();
        Type type = new Type("1", "Type");

        Relocation relocation1 = new Relocation(areaList, districtList, type);
        Relocation relocation2 = new Relocation();

        assertEquals(areaList, relocation1.getArea());
        assertEquals(districtList, relocation1.getDistrict());
        assertEquals(type, relocation1.getType());

        assertNull(relocation2.getArea());
        assertNull(relocation2.getDistrict());
        assertNull(relocation2.getType());
    }

    @Test
    public void equalsAndHashCodeTest() {
        List<Area> areaList1 = new ArrayList<>();
        List<Type> districtList1 = new ArrayList<>();
        Type type1 = new Type("1", "Type");

        List<Area> areaList2 = new ArrayList<>();
        List<Type> districtList2 = new ArrayList<>();
        Type type2 = new Type("1", "Type");

        Relocation relocation1 = new Relocation(areaList1, districtList1, type1);
        Relocation relocation2 = new Relocation(areaList2, districtList2, type2);

        assertEquals(relocation1, relocation2);
        assertEquals(relocation1.hashCode(), relocation2.hashCode());

        areaList2.add(new Area("1", "Area"));
        assertNotEquals(relocation1, relocation2);
        assertNotEquals(relocation1.hashCode(), relocation2.hashCode());
    }

    @Test
    public void toStringTest() {
        List<Area> areaList = new ArrayList<>();
        List<Type> districtList = new ArrayList<>();
        Type type = new Type("1", "Type");

        Relocation relocation = new Relocation(areaList, districtList, type);

        String expected = "Relocation(area=" + areaList + ", district=" + districtList + ", type=" + type + ")";
        assertEquals(expected, relocation.toString());
    }

    @Test
    public void createEntityFromJsonTest() {
        String json = "{\"area\": [], \"district\": [], \"type\": {\"id\": \"1\", \"name\": \"Type\"}}";

        Relocation relocation = JsonProcessor.createEntityFromJson(json, Relocation.class);

        assertEquals(new ArrayList<>(), relocation.getArea());
        assertEquals(new ArrayList<>(), relocation.getDistrict());
        assertEquals(new Type("1", "Type"), relocation.getType());
    }
}
