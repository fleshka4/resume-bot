package com.resume.util;

import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.Locale;
import com.resume.bot.json.entity.Skills;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.bot.json.entity.common.Type;
import com.resume.bot.json.entity.metro.Metro;
import com.resume.bot.json.entity.roles.ProfessionalRoles;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class Constants {
    public static List<Area> AREAS;
    public static List<Country> COUNTRIES;
    public static List<Metro> METROS;
    public static List<Industry> INDUSTRIES;
    public static ProfessionalRoles PROFESSIONAL_ROLES;
    public static Skills SKILLS;
    public static List<Locale> LOCALES;
    public static List<Type> LANGUAGES;
    public static final String ITEMS_DELIMITER = ";";
    public static final String EXPERIENCE_DELIMITER = " - ";

    public static final String OTHER_COUNTRIES_JSON_ID = "1001";
    public static final List<String> COUNTRIES_WITH_REGIONS_IDS = List.of(
            "113",   // Россия
            "16",    // Беларусь
            "5"      // Украина
    );

    public static Map<String, String> visibilityTypes = Map.of(
            "hidden_to_no_one", "не видно никому",
            "visible_to_selected_companies", "видно выбранным компаниям",
            "hidden_from_selected_companies", "скрыто от выбранных компаний",
            "visible_to_all_registered_companies", "видно всем компаниям, зарегистрированным на HeadHunter",
            "visible_to_entire_internet", "видно всему интернету",
            "accessible_only_by_direct_link", "доступно только по прямой ссылке");
    public static Map<String, String> tripReadinessTypes = Map.of(
            "ready", "готов к командировкам",
            "sometimes", "готов к редким командировкам",
            "never", "не готов к командировкам");
    public static Map<String, String> driverLicenseTypes = Map.of(
            "A", "A",
            "B", "B",
            "C", "C",
            "D", "D",
            "E", "E",
            "BE", "BE",
            "CE", "CE",
            "DE", "DE",
            "TM", "TM",
            "TB", "TB");
    public static Map<String, String> employmentTypes = Map.of(
            "full", "Полная занятость",
            "part", "Частичная занятость",
            "project", "Проектная работа",
            "volunteer", "Волонтерство",
            "probation", "Стажировка");
    public static Map<String, String> hiddenFieldTypes = Map.of(
            "name_and_photo", "ФИО и фотографию",
            "phones", "Все указанные в резюме телефоны",
            "email", "Электронную почту",
            "other_contacts", "Прочие контакты (Skype, ICQ, соцсети)",
            "experience", "Все места работы");
    public static Map<String, String> scheduleTypes = Map.of(
            "fullDay", "Полный день",
            "shift", "Сменный график",
            "flexible", "Гибкий график",
            "remote", "Удаленная работа",
            "flyInFlyOut", "Вахтовый метод");
    public static Map<String, String> travelTimeTypes = Map.of(
            "any", "Не имеет значения",
            "less_than_hour", "Не более часа",
            "from_hour_to_one_and_half", "Не более полутора часов");
    public static Map<String, String> sexTypes = Map.of(
            "male", "Мужской",
            "female", "Женский");
    public static Map<String, String> relocationReadinessTypes = Map.of(
            "no_relocation", "не готов к переезду",
            "relocation_possible", "готов к переезду",
            "relocation_desirable", "хочу переехать");
    public static List<String> currencies = List.of(
            "Манаты",
            "Белорусские рубли",
            "Евро",
            "Грузинский лари",
            "Киргизский сом",
            "Тенге",
            "Рубли",
            "Гривны",
            "Доллары",
            "Узбекский сум");
    public static Map<String, String> siteTypes = Map.of(
            "personal", "Другой сайт",
            "moi_krug", "Мой круг",
            "livejournal", "LiveJournal",
            "linkedin", "LinkedIn",
            "freelance", "Free-lance",
            "skype", "Skype",
            "icq", "ICQ");
    public static Map<String, String> contactTypes = Map.of(
            "home", "Домашний телефон",
            "work", "Рабочий телефон",
            "cell", "Мобильный телефон",
            "email", "Эл. почта");
    public static Map<String, String> educationLevels = Map.of(
            "secondary", "Среднее",
            "special_secondary", "Среднее специальное",
            "unfinished_higher", "Неоконченное высшее",
            "higher", "Высшее",
            "bachelor", "Бакалавр",
            "master", "Магистр",
            "candidate", "Кандидат наук",
            "doctor", "Доктор наук");
    public static Map<String, String> languageLevels = Map.of(
            "a1", "A1 — Начальный",
            "a2", "A2 — Элементарный",
            "b1", "B1 — Средний",
            "b2", "B2 — Средне-продвинутый",
            "c1", "C1 — Продвинутый",
            "c2", "C2 — В совершенстве",
            "l1", "Родной");
}
