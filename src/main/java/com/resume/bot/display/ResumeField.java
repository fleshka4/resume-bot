package com.resume.bot.display;

import com.resume.bot.json.JsonValidator;
import com.resume.bot.json.entity.client.*;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import com.resume.util.Constants;
import com.resume.util.ConstantsUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum ResumeField {
    NAME("имя") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkName(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setFirstName(param);
        }
    },

    SURNAME("фамилия") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkName(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setLastName(param);
        }
    },

    PATRONYMIC("отчество") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkName(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setMiddleName(param);
        }
    },

    SEX("пол") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSex(checkValue);
        }

        @Override
        public String message() {
            return "Пропаганда ЛГБТ запрещена УК РФ ст.282!";
        }

        @Override
        public void editInResume(Resume resume, String param) {
            Constants.sexTypes.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(param))
                    .findFirst()
                    .ifPresent(gender -> resume.setGender(new Id(gender.getKey())));
        }
    },

    BIRTHDAY("дата рождения") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkBirthday(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_DATE_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setBirthDate(param);
        }
    },

    LIVE_LOCATION("место жительства") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256) && JsonValidator.checkLocation(checkValue);
        }

        @Override
        public String message() {
            return formatLocation(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            ConstantsUtil.getAreaByNameDeep(Constants.AREAS, param)
                    .ifPresent(area -> resume.setArea(new Id(area.getId())));
        }
    },

    EDUCATION_LEVEL("уровень образования") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkEducationLevel(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_PARAM_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            Constants.educationLevels.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(param))
                    .findFirst().ifPresent(level -> resume.getEducation().setLevel(new Type(level.getKey(), level.getValue())));
        }
    },

    EDUCATION_INSTITUTION("учебное заведение") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_512);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_512);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<PrimaryEducation> list = resume.getEducation().getPrimary();
            if (!list.isEmpty()) {
                list.getFirst().setName(param);
            }
        }
    },

    EDUCATION_FACULTY("факультет") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<PrimaryEducation> list = resume.getEducation().getPrimary();
            if (!list.isEmpty()) {
                list.getFirst().setOrganization(param);
            }
        }
    },

    EDUCATION_SPECIALIZATION("специализация") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<PrimaryEducation> list = resume.getEducation().getPrimary();
            if (!list.isEmpty()) {
                list.getFirst().setResult(param);
            }
        }
    },

    EDUCATION_END_YEAR("год окончания") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkGraduationYear(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_NUMBER_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<PrimaryEducation> list = resume.getEducation().getPrimary();
            if (!list.isEmpty()) {
                list.getFirst().setYear(Long.getLong(param, LocalDate.now().getYear()));
            }
        }
    },

    EXPERIENCE_PERIOD("опыт работы") {
        @Override
        public boolean processCheck(String checkValue) {
            try {
                String[] dates = checkValue.split("-", 2);
                return JsonValidator.checkExperience(dates[0].trim(), dates[1].trim());
            } catch (RuntimeException e) {
                return false;
            }
        }

        @Override
        public String message() {
            return INCORRECT_PERIOD_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Experience> list = resume.getExperience();
            if (!list.isEmpty()) {
                list.getFirst().setCompany(param);
            }
        }
    },

    EXPERIENCE_ORG_NAME("название организации") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_512);
        }

        @Override
        public String message() {
            return EXCEEDED_SYMBOLS_LIMIT_STRING.formatted(getValue(), MAX_LEN_512);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Experience> list = resume.getExperience();
            if (!list.isEmpty()) {
                list.getFirst().setCompany(param);
            }
        }
    },

    EXPERIENCE_ORG_CITY("город организации") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256) && JsonValidator.checkCity(checkValue);
        }

        @Override
        public String message() {
            return formatLocation(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Experience> list = resume.getExperience();
            if (!list.isEmpty()) {
                ConstantsUtil.getAreaByNameDeep(Constants.AREAS, param)
                        .ifPresent(city -> list.getFirst().setArea(new Area(city.getId(), city.getName())));
            }
        }
    },

    EXPERIENCE_ORG_INDUSTRY("отрасль") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256);
        }

        @Override
        public String message() {
            return formatLocation(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            /* List<Experience> list = resume.getExperience();
            if (!list.isEmpty()) {
                Constants.INDUSTRIES.stream()
                        .map(Industry::getIndustries).flatMap(List::stream)
                        .filter(ind -> ind.getName().equals(param)).findFirst()
                        .ifPresent(industry -> list.getFirst().setIndustry(industry));
            }*/
        }
    },

    EXPERIENCE_ORG_LINK("ссылка") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256) && JsonValidator.checkLinkFormat(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_HYPERLINK_STRING.formatted(MAX_LEN_256);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Experience> list = resume.getExperience();
            if (!list.isEmpty()) {
                list.getFirst().setCompanyUrl(param);
            }
        }
    },

    EXPERIENCE_POST("должность") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Experience> list = resume.getExperience();
            if (!list.isEmpty()) {
                list.getFirst().setPosition(param);
            }
        }
    },

    EXPERIENCE_DUTIES("обязанности") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_4096);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_4096);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Experience> list = resume.getExperience();
            if (!list.isEmpty()) {
                list.getFirst().setDescription(param);
            }
        }
    },

    SKILLS("навыки") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setSkillSet(Arrays.stream(param.split(",")).collect(Collectors.toSet()));
        }
    },

    ABOUT_ME("о себе") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_4096);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_4096);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setSkills(param);
        }
    },

    DRIVER_LICENCE("категория прав") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkDriverLicenseType(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_PARAM_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            if (JsonValidator.checkDriverLicenseType(param)) {
                resume.getDriverLicenseTypes().add(new Id(param));
            }
        }
    },

    REC_NAME("имя выдавшего рекомендацию") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_512) && JsonValidator.checkName(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_512);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Recommendation> list = resume.getRecommendation();
            if (!list.isEmpty()) {
                list.getFirst().setName(param);
            }
        }
    },

    REC_POST("должность выдавшего рекомендацию") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Recommendation> list = resume.getRecommendation();
            if (!list.isEmpty()) {
                list.getFirst().setPosition(param);
            }
        }
    },

    REC_ORGANIZATION("организация выдавшего рекомендацию") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Recommendation> list = resume.getRecommendation();
            if (!list.isEmpty()) {
                list.getFirst().setOrganization(param);
            }
        }
    },

    WISH_POSITION("желаемая позиция") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setTitle(param);
        }
    },

    WITH_PROFESSIONAL_ROLE("профессиональная роль") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256);
        }

        @Override
        public String message() {
            return formatLocation(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            // NOOP
        }
    },

    WISH_SALARY("желаемая зарплата") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkNumericFormat(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_SALARY_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            Salary salary = resume.getSalary();
            salary.setAmount(Long.getLong(param, salary.getAmount()));
        }
    },

    BUSYNESS("желаемая занятость") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkEmploymentType(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_PARAM_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Type> employments = resume.getEmployments();
            Map.Entry<String, String> matchedEntry = Constants.employmentTypes.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(param)).findFirst().orElse(null);
            if (employments.stream().noneMatch(emp -> emp.getName().equals(param))
                    && matchedEntry != null) {
                resume.getSchedules().add(new Type(matchedEntry.getKey(), matchedEntry.getValue()));
            }
        }
    },

    SCHEDULE("желаемый график") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkScheduleType(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_PARAM_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            List<Type> schedules = resume.getSchedules();
            Map.Entry<String, String> matchedEntry = Constants.scheduleTypes.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(param)).findFirst().orElse(null);
            if (schedules.stream().noneMatch(sch -> sch.getName().equals(param))
                    && matchedEntry != null) {
                resume.getSchedules().add(new Type(matchedEntry.getKey(), matchedEntry.getValue()));
            }
        }
    },

    PHONE("контакты") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkPhoneFormat(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_PARAM_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setContacts(param);
        }
    },
    EMAIL("контакты") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkEmailFormat(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_PARAM_STRING.formatted(getValue());
        }

        @Override
        public void editInResume(Resume resume, String param) {
            resume.setContacts(param);
        }
    };

    private static final short MAX_LEN_128 = 128;
    private static final short MAX_LEN_256 = 256;
    private static final short MAX_LEN_512 = 512;
    private static final short MAX_LEN_4096 = 4096;

    private static final String MUST_CONTAIN_LETTERS_STRING = "Параметр \"%s\" должен состоять только из букв! Также, число символов не должно превышать %d!";
    private static final String INCORRECT_PARAM_WITH_SYMBOLS_STRING = "Параметр \"%s\" указан некорректно! Также, число символов не должно превышать %d!";
    private static final String INCORRECT_PARAM_STRING = "Параметр \"%s\" указан некорректно!";
    private static final String INCORRECT_LOCATION_STRING = "Параметр \"%s\" указан неверно! Также, число символов не должно превышать %d!";
    private static final String EXCEEDED_SYMBOLS_LIMIT_STRING = "В параметре \"%s\" превышено число символов! Также, число символов не должно превышать %d!";
    private static final String INCORRECT_HYPERLINK_STRING = "Ссылка на организацию написана некорректно! Также, число символов не должно превышать %d!";
    private static final String INCORRECT_NUMBER_STRING = "В параметре \"%s\" указан некорректный год!";
    private static final String INCORRECT_PERIOD_STRING = "В параметре \"%s\" указан некорректный период!";
    private static final String INCORRECT_DATE_STRING = "Параметр \"%s\" содержит в себе некорректную дату!";
    private static final String INCORRECT_SALARY_STRING = "В параметре \"%s\" указано некорректное значение!";

    private final String value;

    abstract public boolean processCheck(String checkValue);

    abstract public String message();

    abstract public void editInResume(Resume resume, String param);

    private static String formatLetters(String string, short maxLen) {
        return MUST_CONTAIN_LETTERS_STRING.formatted(string, maxLen);
    }

    private static String formatLocation(String string) {
        return INCORRECT_LOCATION_STRING.formatted(string, MAX_LEN_256);
    }
}
