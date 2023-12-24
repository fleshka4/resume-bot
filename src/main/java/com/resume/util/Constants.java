package com.resume.util;

import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.Locale;
import com.resume.bot.json.entity.Skills;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.bot.json.entity.metro.Metro;
import com.resume.bot.json.entity.roles.ProfessionalRoles;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {
    public static List<Area> AREAS;
    public static List<Country> COUNTRIES;
    public static List<Metro> METROS;
    public static List<Industry> INDUSTRIES;
    public static ProfessionalRoles PROFESSIONAL_ROLES;
    public static Skills SKILLS;
    public static List<Locale> LOCALES;

    public static String[] visibilityTypes = new String[]{
            "не видно никому",
            "видно выбранным компаниям",
            "скрыто от выбранных компаний",
            "видно всем компаниям, зарегистрированным на HeadHunter",
            "видно всему интернету",
            "доступно только по прямой ссылке"};
    public static String[] tripReadinessTypes = new String[]{
            "готов к командировкам",
            "готов к редким командировкам",
            "не готов к командировкам"};
    public static String[] driverLicenseTypes = new String[]{
            "A",
            "B",
            "C",
            "D",
            "E",
            "BE",
            "CE",
            "DE",
            "TM",
            "TB"};
    public static String[] employmentTypes = new String[]{
            "Полная занятость",
            "Частичная занятость",
            "Проектная работа",
            "Волонтерство",
            "Стажировка"};
    public static String[] hiddenFieldTypes = new String[]{
            "ФИО и фотографию",
            "Все указанные в резюме телефоны",
            "Электронную почту",
            "Прочие контакты (Skype, ICQ, соцсети)",
            "Все места работы"};
    public static String[] scheduleTypes = new String[]{
            "Полный день",
            "Сменный график",
            "Гибкий график",
            "Удаленная работа",
            "Вахтовый метод"};
    public static String[] travelTimeTypes = new String[]{
            "Не имеет значения",
            "Не более часа",
            "Не более полутора часов"};
    public static String[] sexTypes = new String[]{
            "Мужской",
            "Женский"};
    public static String[] relocationReadinessTypes = new String[]{
            "не готов к переезду",
            "готов к переезду",
            "хочу переехать"};
    public static String[] currencies = new String[]{
            "Манаты",
            "Белорусские рубли",
            "Евро",
            "Грузинский лари",
            "Киргизский сом",
            "Тенге",
            "Рубли",
            "Гривны",
            "Доллары",
            "Узбекский сум"};
    public static String[] siteTypes = new String[]{
            "Другой сайт",
            "Мой круг",
            "LiveJournal",
            "LinkedIn",
            "Free-lance",
            "Skype",
            "ICQ"};
    public static String[] contactTypes = new String[]{
            "Домашний телефон",
            "Рабочий телефон",
            "Мобильный телефон",
            "Эл. почта"};
    public static String[] educationLevels = new String[]{
            "Среднее",
            "Среднее специальное",
            "Неоконченное высшее",
            "Высшее",
            "Бакалавр",
            "Магистр",
            "Кандидат наук",
            "Доктор наук"};
    public static String[] languageLevels = new String[]{
            "A1 — Начальный",
            "A2 — Элементарный",
            "B1 — Средний",
            "B2 — Средне-продвинутый",
            "C1 — Продвинутый",
            "C2 — В совершенстве",
            "Родной"};
}
