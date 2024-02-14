package com.resume.latex;

import com.resume.bot.json.JsonValidator;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Placeholder {
    PLACE_FOR_NAME("{PLACE-FOR-NAME}") {
        @Override
        public String replaceValue(Resume resume) {
            return "%s %s %s".formatted(resume.getLastName(), resume.getFirstName(), resume.getMiddleName());
        }
    },
    PLACE_FOR_POSITION("{PLACE-FOR-POSITION}") {
        @Override
        public String replaceValue(Resume resume) {
            return resume.getTitle();
        }
    },
    PLACE_FOR_NUMBER("{PLACE-FOR-NUMBER}") {
        @Override
        public String replaceValue(Resume resume) {
            String contacts = resume.getContacts();
            return !contacts.contains("@") ? contacts : "";
        }
    },
    PLACE_FOR_EMAIL("{PLACE-FOR-EMAIL}") {
        @Override
        public String replaceValue(Resume resume) {
            String contacts = resume.getContacts();
            return contacts.contains("@") ? contacts : "";
        }
    },
    PLACE_FOR_CITY("{PLACE-FOR-CITY}") {
        @Override
        public String replaceValue(Resume resume) {
            String[] areaParts = JsonValidator.getAreaString(
                    JsonValidator.getAreaByIdDeep(Constants.AREAS, resume.getArea().getId())
                            .get()).split(",");
            return areaParts.length == 2
                    ? areaParts[1].trim()
                    : areaParts.length == 3
                    ? areaParts[2].trim()
                    : "";
        }
    },
    PLACE_FOR_COUNTRY("{PLACE-FOR-COUNTRY}") {
        @Override
        public String replaceValue(Resume resume) {
            return JsonValidator.getAreaString(
                    JsonValidator.getAreaByIdDeep(Constants.AREAS, resume.getArea().getId())
                            .get()).split(",")[0].trim();
        }
    },
    PLACE_FOR_SKILLS("{PLACE-FOR-SKILLS}") {
        @Override
        public String replaceValue(Resume resume) {
            return resume.getSkills();
        }
    },
    PLACE_FOR_EDUCATION("{PLACE-FOR-EDUCATION}") {
        @Override
        public String replaceValue(Resume resume) {
            Education education = resume.getEducation();
            return """
                    Уровень образования: %s
                                        
                    %s
                    """.formatted(
                    education.getLevel().getName(),
                    education.getPrimary().stream()
                            .map(pe -> """
                                    Учебное заведение: %s
                                    Факультет: %s
                                    Специализация: %s
                                    Год окончания: %d
                                                                        
                                    """.formatted(
                                    pe.getName(),
                                    pe.getOrganization(),
                                    pe.getResult(),
                                    pe.getYear()
                            ))
                            .collect(Collectors.joining()));
        }
    },
    PLACE_FOR_EXPERIENCE("{PLACE-FOR-EXPERIENCE}") {
        @Override
        public String replaceValue(Resume resume) {
            return resume.getExperience().stream()
                    .map(exp -> """
                            Период работы: %s
                            Компания: %s
                            Город: %s
                            Ссылка: %s
                            Должность: %s
                            Обязанности: %s
                                                                
                            """.formatted(
                            "%s - %s".formatted(exp.getStart(),
                                    exp.getEnd() != null && !exp.getEnd().isEmpty() ? exp.getEnd() : "настоящее время"),
                            exp.getCompany(),
                            JsonValidator.getAreaString(
                                    JsonValidator.getAreaByIdDeep(Constants.AREAS, exp.getArea().getId())
                                            .get()),
                            exp.getCompanyUrl(),
                            exp.getPosition(),
                            exp.getDescription()))
                    .collect(Collectors.joining());
        }
    };

    private final String value;

    abstract public String replaceValue(Resume resume);
}
