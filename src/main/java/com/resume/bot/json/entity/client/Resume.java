package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.resume.bot.json.JsonValidator;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @NonNull
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

    @NonNull
    private Id area;

//    @NonNull
//    private List<Id> citizenship;

//    @NonNull
//    private List<Contact> contact;

    @NonNull
    private Education education;

    @NonNull
    private List<Experience> experience;

    @NonNull
    private Id gender;

    private List<Language> language;

    private String contacts;
}
