package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Phone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneTest {
    @Test
    public void gettersAndSettersTest() {
        String city = "New York";
        String country = "USA";
        String formatted = "+1 (123) 456-7890";
        String number = "1234567890";

        Phone phone = new Phone();
        phone.setCity(city);
        phone.setCountry(country);
        phone.setFormatted(formatted);
        phone.setNumber(number);

        assertEquals(phone.getCity(), city);
        assertEquals(phone.getCountry(), country);
        assertEquals(phone.getFormatted(), formatted);
        assertEquals(phone.getNumber(), number);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Phone phone = new Phone();

        assertThrows(NullPointerException.class, () -> phone.setCity(null));
        assertThrows(NullPointerException.class, () -> phone.setCountry(null));
        assertThrows(NullPointerException.class, () -> phone.setFormatted(null));
        assertThrows(NullPointerException.class, () -> phone.setNumber(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String city = "San Francisco";
        String country = "USA";
        String formatted = "+1 (987) 654-3210";
        String number = "9876543210";

        Phone phone = new Phone(city, country, formatted, number);

        assertEquals(phone.getCity(), city);
        assertEquals(phone.getCountry(), country);
        assertEquals(phone.getFormatted(), formatted);
        assertEquals(phone.getNumber(), number);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Phone(null, "USA", "+1 (123) 456-7890", "1234567890"));
        assertThrows(NullPointerException.class, () -> new Phone("New York", null, "+1 (123) 456-7890", "1234567890"));
        assertThrows(NullPointerException.class, () -> new Phone("New York", "USA", null, "1234567890"));
        assertThrows(NullPointerException.class, () -> new Phone("New York", "USA", "+1 (123) 456-7890", null));
    }

    @Test
    public void noArgsConstructorTest() {
        Phone phone = new Phone();

        assertNull(phone.getCity());
        assertNull(phone.getCountry());
        assertNull(phone.getFormatted());
        assertNull(phone.getNumber());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Phone phone1 = new Phone("New York", "USA", "+1 (123) 456-7890", "1234567890");
        Phone phone2 = new Phone("New York", "USA", "+1 (123) 456-7890", "1234567890");
        Phone phone3 = new Phone("San Francisco", "USA", "+1 (987) 654-3210", "9876543210");

        assertEquals(phone1, phone2);
        assertNotEquals(phone1, phone3);

        assertEquals(phone1.hashCode(), phone2.hashCode());
        assertNotEquals(phone1.hashCode(), phone3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Phone phone = new Phone("New York", "USA", "+1 (123) 456-7890", "1234567890");

        String expectedToString = "Phone(city=New York, country=USA, formatted=+1 (123) 456-7890, number=1234567890)";
        assertEquals(expectedToString, phone.toString());
    }

    @Test
    public void createPhoneFromJsonTest() {
        String json = """
                {
                  "city": "Los Angeles",
                  "country": "USA",
                  "formatted": "+1 (555) 555-5555",
                  "number": "5555555555"
                }
                """;

        Phone phone = JsonProcessor.createEntityFromJson(json, Phone.class);

        assertEquals(phone.getCity(), "Los Angeles");
        assertEquals(phone.getCountry(), "USA");
        assertEquals(phone.getFormatted(), "+1 (555) 555-5555");
        assertEquals(phone.getNumber(), "5555555555");
    }

    @Test
    public void createPhoneWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Phone.class));
    }

    @Test
    public void fullPhoneFill() {
        String country = "7";
        String city = "812";
        String number = "8883344";
        String formatted = country + city + number;
        Phone entity = JsonProcessor.createEntityFromJson("""
                {
                  "city": "%s",
                  "country": "%s",
                  "number": "%s",
                  "formatted": "%s"
                }""".formatted(city, country, number, formatted), Phone.class);

        assertEquals(entity.getClass(), Phone.class);
        assertEquals(entity.getCountry(), country);
        assertEquals(entity.getCity(), city);
        assertEquals(entity.getNumber(), number);
        assertEquals(entity.getFormatted(), formatted);
    }

    @Test
    public void countryPhoneFillFail() {
        String country = "7";

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                  "country": "%s"
                }""".formatted(country), Phone.class));
    }

    @Test
    public void cityPhoneFillFail() {
        String city = "812";

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                  "city": "%s"
                }""".formatted(city), Phone.class));
    }

    @Test
    public void numberPhoneFillFail() {
        String number = "8883344";

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                  "number": "%s"
                }""".formatted(number), Phone.class));
    }

    @Test
    public void formattedPhoneFillFail() {
        String formatted = "78128883344";

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                  "formatted": "%s"
                }""".formatted(formatted), Phone.class));
    }

    @Test
    public void emptyPhoneFillFail() {
        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {}""", Phone.class));
    }
}
