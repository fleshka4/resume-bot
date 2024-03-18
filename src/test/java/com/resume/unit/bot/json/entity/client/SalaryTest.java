package com.resume.unit.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Salary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SalaryTest {
    @Test
    public void gettersAndSettersTest() {
        Salary salary = new Salary();

        assertNull(salary.getAmount());
        assertNull(salary.getCurrency());

        Long newAmount = 50000L;
        String newCurrency = "USD";

        salary.setAmount(newAmount);
        salary.setCurrency(newCurrency);

        assertEquals(newAmount, salary.getAmount());
        assertEquals(newCurrency, salary.getCurrency());
    }

    @Test
    public void noArgsConstructorTest() {
        Salary salary = new Salary();

        assertNull(salary.getAmount());
        assertNull(salary.getCurrency());
    }

    @Test
    public void allArgsConstructorTest() {
        Long amount = 75000L;
        String currency = "EUR";

        Salary salary = new Salary(amount, currency);

        assertEquals(amount, salary.getAmount());
        assertEquals(currency, salary.getCurrency());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Long amount = 75000L;
        String currency = "EUR";

        Salary salary1 = new Salary(amount, currency);
        Salary salary2 = new Salary(amount, currency);
        Salary salary3 = new Salary(60000L, "USD");

        assertEquals(salary1, salary2);
        assertNotEquals(salary1, salary3);

        assertEquals(salary1.hashCode(), salary2.hashCode());
        assertNotEquals(salary1.hashCode(), salary3.hashCode());
    }

    @Test
    public void toStringTest() {
        Long amount = 75000L;
        String currency = "EUR";

        Salary salary = new Salary(amount, currency);

        String expected = "Salary(amount=" + amount + ", currency=" + currency + ")";
        assertEquals(expected, salary.toString());
    }

    @Test
    public void createEntityFromJsonTest() {
        String json = "{\"amount\": 50000, \"currency\": \"GBP\"}";

        Salary salary = JsonProcessor.createEntityFromJson(json, Salary.class);

        assertEquals(50000L, salary.getAmount());
        assertEquals("GBP", salary.getCurrency());
    }
}
