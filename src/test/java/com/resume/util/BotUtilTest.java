package com.resume.util;

import com.resume.bot.json.entity.client.Experience;
import com.resume.bot.json.entity.client.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BotUtilTest {
    private final String falseString = "asd1231";

    @Test
    public void checkIfActionTest() {
        for (String act : BotUtil.ACTIONS_WITH_RESUME) {
            Assertions.assertTrue(BotUtil.checkIfAction(act));
        }

        Assertions.assertFalse(BotUtil.checkIfAction(falseString));
    }

    @Test
    public void checkIfBigTypeTest() {
        for (String act : BotUtil.BIG_TYPES_IDS) {
            Assertions.assertTrue(BotUtil.checkIfBigType(act));
        }

        Assertions.assertFalse(BotUtil.checkIfBigType(falseString));
    }

    @Test
    public void convertResumeTest() {
        Resume before = new Resume();

        String startFirst = "01-2024";
        String endFirst = "03-2024";
        String startSecond = "03-2024";

        String expStartFirst = "2024-01-01";
        String expEndFirst = "2024-03-01";
        String expStartSecond = "2024-03-01";

        before.setExperience(new ArrayList<>() {{
            add(new Experience() {{
                setStart(startFirst);
                setEnd(endFirst);
            }});
            add(new Experience() {{
                setStart(startSecond);
            }});
        }});

        Resume after = BotUtil.convertResume(before);

        List<Experience> afterExperience = after.getExperience();
        Assertions.assertEquals(expStartFirst, afterExperience.get(0).getStart());
        Assertions.assertEquals(expEndFirst, afterExperience.get(0).getEnd());
        Assertions.assertEquals(expStartSecond, afterExperience.get(1).getStart());
    }

    @Test
    public void createSortedMapTest() {
        Map<String, String> map = Map.of(
                "bcda", "456",
                "dbcs", "789",
                "abcd", "123"
                );

        Assertions.assertEquals(BotUtil.createSortedMap(map).getClass(), TreeMap.class);
        Assertions.assertThrows(NullPointerException.class, () -> BotUtil.createSortedMap(null));
    }

    @Test
    public void getResumeNumberTest() {
        String rightString = "abc_1";
        Assertions.assertEquals(BotUtil.getResumeNumber(rightString), 1);
        Assertions.assertThrows(NumberFormatException.class, () -> BotUtil.getResumeNumber(falseString));
    }
}
