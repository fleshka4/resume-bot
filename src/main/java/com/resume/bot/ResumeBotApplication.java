package com.resume.bot;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ResumeBotApplication {
    public static void main(String[] args) {
//        SpringApplication.run(ResumeBotApplication.class, args);

        Client b = new Client();
        b.setGender(new Id("Male"));
        b.setProfessionalRoles(new ArrayList<>());
        b.setArea(new Id("1"));
        b.setCitizenship(new ArrayList<>());

        List<Contact> contactList = new ArrayList<>();
        ValuePhoneOrEmail phone = new ValuePhoneOrEmail();
        phone.setPhone(new Phone("1", "2", "3", "89111371728"));

        ValuePhoneOrEmail email = new ValuePhoneOrEmail();
        email.setEmail("gagaga@gsdjas.ru");

        contactList.add(new Contact("abcabc", null, true, new Type("1", "phone"), phone, null));
        contactList.add(new Contact(null, null, false, new Type("2", "email"), email, null));

        b.setContact(contactList);
        b.setEducation(new Education());
        b.setExperience(new ArrayList<>());
        b.setLanguage(new ArrayList<>());

        String jsonString = JsonProcessor.createJsonFromEntity(b);

        Client a = JsonProcessor.createEntityFromJson(jsonString, Client.class);
        System.out.println(a.getGender().getId());

        System.out.println(jsonString);
    }
}
