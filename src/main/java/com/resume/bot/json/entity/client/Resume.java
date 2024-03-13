package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Resume {
    @JsonProperty("alternate_url")
    private String alternateUrl;

    @JsonProperty("download")
    private Download download;

    @JsonProperty("birth_date")
    private String birthDate;

//    @JsonProperty("business_trip_readiness")
//    private Id businessTripReadiness;
//
//    private List<Certificate> certificate;
//
    @JsonProperty("driver_license_types")
    private List<Id> driverLicenseTypes;

    private List<Type> employments;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("has_vehicle")
    private Boolean hasVehicle;

    @JsonProperty("last_name")
    private String lastName;

    //    private Id metro;
//
    @JsonProperty("middle_name")
    private String middleName;

    //    private Photo photo;
//
//    private List<Portfolio> portfolio;
//
//    @NonNull
    @JsonProperty("professional_roles")
    private List<Id> professionalRoles;

    private List<Recommendation> recommendation;

//    private Relocation relocation;

    private Salary salary;

    private List<Type> schedules;

    //    private List<Site> site;
//
    @JsonProperty("skill_set")
    private Set<String> skillSet;

    private String skills;

    private String title;

    @JsonProperty("total_experience")
    private TotalExperience totalExperience;

//    @JsonProperty("travel_time")
//    private Id travelTime;
//
//    @JsonProperty("work_ticket")
//    private List<Id> workTicket;

    private Id area;

//    @NonNull
//    private List<Id> citizenship;

//    @NonNull
//    private List<Contact> contact;

    private Education education;

    private List<Experience> experience;

    private Id gender;

    private List<Language> language;

    private String contacts;

    @JsonCreator
    public static Resume createResume(
            @JsonProperty("alternate_url") String alternateUrl,
            @JsonProperty("download") Download download,
            @JsonProperty("birth_date") String birthDate,
            @JsonProperty("driver_license_types") List<Id> driverLicenseTypes,
            @JsonProperty("employments") List<Type> employments,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("has_vehicle") Boolean hasVehicle,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("middle_name") String middleName,
            @JsonProperty("professional_roles") List<Id> professionalRoles,
            @JsonProperty("recommendation") List<Recommendation> recommendation,
            @JsonProperty("salary") Salary salary,
            @JsonProperty("schedules") List<Type> schedules,
            @JsonProperty("skill_set") Set<String> skillSet,
            @JsonProperty("skills") String skills,
            @JsonProperty("title") String title,
            @JsonProperty("total_experience") TotalExperience totalExperience,
            @JsonProperty("area") Id area,
            @JsonProperty("education") Education education,
            @JsonProperty("experience") List<Experience> experience,
            @JsonProperty("gender") Id gender,
            @JsonProperty("language") List<Language> language,
            @JsonProperty("contacts") String contacts) {
        return new Resume(
                alternateUrl, download, birthDate, driverLicenseTypes, employments,
                firstName, hasVehicle, lastName, middleName, professionalRoles,
                recommendation, salary, schedules, skillSet, skills, title,
                totalExperience, area, education, experience, gender, language, contacts
        );
    }
}
