package com.resume.latex;

import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.client.Experience;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.client.education.Course;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.client.education.ElementaryEducation;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import com.resume.bot.json.entity.common.Id;
import com.resume.util.Constants;
import com.resume.util.ConstantsUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Placeholder {
    PLACE_FOR_NAME("$$PLACE-FOR-NAME$$") {
        @Override
        public String replaceValue(Resume resume) {
            return "%s %s %s".formatted(resume.getLastName(), resume.getFirstName(), resume.getMiddleName());
        }
    },
    PLACE_FOR_BIRTHDAY("$$PLACE-FOR-BIRTHDAY$$") {
        @Override
        public String replaceValue(Resume resume) {
            return resume.getBirthDate();
        }
    },
    PLACE_FOR_VEHICLE("$$PLACE-FOR-VEHICLE$$") {
        @Override
        public String replaceValue(Resume resume) {
            Boolean has = resume.getHasVehicle();
            return has != null
                    ? has
                    ? "есть"
                    : "нет"
                    : "";
        }
    },
    PLACE_FOR_DRIVER_LICENSE("$$PLACE-FOR-DRIVER-LICENSE$$") {
        @Override
        public String replaceValue(Resume resume) {
            return resume.getDriverLicenseTypes().stream().map(Id::getId).collect(Collectors.joining(", "));
        }
    },
    PLACE_FOR_POSITION("$$PLACE-FOR-POSITION$$") {
        @Override
        public String replaceValue(Resume resume) {
            return resume.getTitle();
        }
    },
    PLACE_FOR_NUMBER("$$PLACE-FOR-NUMBER$$") {
        @Override
        public String replaceValue(Resume resume) {
            String contacts = resume.getContacts();
            return contacts != null && !contacts.contains("@") ? contacts : "";
        }
    },
    PLACE_FOR_EMAIL("$$PLACE-FOR-EMAIL$$") {
        @Override
        public String replaceValue(Resume resume) {
            String contacts = resume.getContacts();
            return contacts != null && contacts.contains("@") ? contacts : "";
        }
    },
    PLACE_FOR_CITY("$$PLACE-FOR-CITY$$") {
        @Override
        public String replaceValue(Resume resume) {
            Area area = ConstantsUtil.getAreaByIdDeep(Constants.AREAS, resume.getArea().getId()).orElse(null);
            if (area == null) {
                return "";
            }

            String[] areaParts = ConstantsUtil.getAreaString(area).split(",");
            return areaParts.length == 2
                    ? areaParts[1].trim()
                    : areaParts.length == 3
                    ? areaParts[2].trim()
                    : "";
        }
    },
    PLACE_FOR_COUNTRY("$$PLACE-FOR-COUNTRY$$") {
        @Override
        public String replaceValue(Resume resume) {
            Area area = ConstantsUtil.getAreaByIdDeep(Constants.AREAS, resume.getArea().getId()).orElse(null);
            if (area == null) {
                return "";
            }

            return ConstantsUtil.getAreaString(area).split(",")[0].trim();
        }
    },
    PLACE_FOR_ABOUT("$$PLACE-FOR-ABOUT$$") {
        @Override
        public String replaceValue(Resume resume) {
            String skills = resume.getSkills();
            return skills != null ? skills : "";
        }
    },
    PLACE_FOR_EDUCATION("$$PLACE-FOR-EDUCATION$$") {
        @Override
        public String replaceValue(Resume resume) {
            Education education = resume.getEducation();
            String levelStr = education.getLevel() != null
                    ? "Уровень образования: %s\\\\".formatted(education.getLevel().getName())
                    : "";

            List<PrimaryEducation> primary = education.getPrimary();
            String primaryStr = primary != null && !primary.isEmpty()
                    ? primary.stream()
                    .map(pe -> """
                            Учебное заведение: %s\\
                            Факультет: %s\\
                            Специализация: %s\\
                            Год окончания: %d\\
                            \\
                            """.formatted(
                            pe.getName(),
                            pe.getOrganization(),
                            pe.getResult(),
                            pe.getYear()
                    ))
                    .collect(Collectors.joining("", "Высшее образование:\\\\", ""))
                    : "";

            List<ElementaryEducation> elementary = education.getElementary();
            String elementaryStr = elementary != null && !elementary.isEmpty()
                    ? elementary.stream()
                    .map(es -> """
                            Учебное заведение: %s\\
                            Год окончания: %d\\
                            \\
                            """.formatted(
                            es.getName(),
                            es.getYear()
                    ))
                    .collect(Collectors.joining("", "Среднее образование:\\\\", ""))
                    : "";

            List<Course> additional = education.getAdditional();
            String additionalStr = additional != null && !additional.isEmpty()
                    ? additional.stream()
                    .map(as -> """
                            Название курса:%s\\
                            Организация: %s\\
                            Специальность: %s\\
                            Год окончания: %d\\
                            \\
                            """.formatted(
                            as.getName(),
                            as.getOrganization(),
                            as.getResult(),
                            as.getYear()
                    ))
                    .collect(Collectors.joining("", "Курсы повышения квалификации:\\\\", ""))
                    : "";

            List<Course> attestation = education.getAttestation();
            String attestationStr = attestation != null && !attestation.isEmpty()
                    ? attestation.stream()
                    .map(as -> """
                            Название курса:%s\\
                            Организация: %s\\
                            Специальность: %s\\
                            Год окончания: %d\\
                            \\
                            """.formatted(
                            as.getName(),
                            as.getOrganization(),
                            as.getResult(),
                            as.getYear()
                    ))
                    .collect(Collectors.joining("", "Пройденные тесты:\\\\", ""))
                    : "";

            return """
                    %s
                    %s
                    %s
                    %s
                    %s
                    """.formatted(levelStr, primaryStr, elementaryStr, additionalStr, attestationStr);
        }
    },
    PLACE_FOR_EXPERIENCE("$$PLACE-FOR-EXPERIENCE$$") {
        @Override
        public String replaceValue(Resume resume) {
            List<Experience> experience = resume.getExperience();
            return !experience.isEmpty()
                    ? experience.stream()
                    .map(exp -> """
                            Период работы: %s\\
                            Компания: %s\\
                            Город: %s\\
                            Ссылка: %s\\
                            Должность: %s\\
                            Обязанности: %s\\
                            \\
                            """.formatted("%s - %s".formatted(
                                    exp.getStart(),
                                    exp.getEnd() != null && !exp.getEnd().isEmpty()
                                            ? exp.getEnd()
                                            : "настоящее время"),
                            exp.getCompany(),
                            ConstantsUtil.getAreaString(
                                    ConstantsUtil.getAreaByIdDeep(Constants.AREAS, exp.getArea().getId()).orElse(null)),
                            exp.getCompanyUrl(),
                            exp.getPosition(),
                            exp.getDescription()))
                    .collect(Collectors.joining())
                    : "";
        }
    },
    PLACE_FOR_TOTAL_EXPERIENCE("$$PLACE-FOR-TOTAL-EXPERIENCE$$") {
        @Override
        public String replaceValue(Resume resume) {
            return resume.getTotalExperience().getMonths().toString();
        }
    };

    private final String value;

    abstract public String replaceValue(Resume resume);
}
