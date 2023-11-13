package com.resume.bot.json;

import com.resume.bot.json.entity.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Client {
    Type access;
    String birthDate;
    Id businessTripReadiness;
    List<Certificate> certificate;
    List<Id> driverLicenseTypes;
    List<Type> employments;
    String firstName;
    Boolean hasVehicle;
    List<Type> hiddenFields;
    String lastName;
    Id metro;
    String middleName;
    Photo photo;
    List<Portfolio> portfolio;
    @NonNull List<Id> professionalRoles;
    List<Recommendation> recommendation;
    Relocation relocation;
    Type resumeLocale;
    Salary salary;
    List<Type> schedules;
    List<Site> site;
    Set<String> skillSet;
    String skills;
    String title;
    TotalExperience totalExperience;
    Id travelTime;
    List<Id> workTicket;
    @NonNull Id area;
    @NonNull List<Id> citizenship;
    @NonNull List<Contact> contact;
    @NonNull Education education;
    @NonNull List<Experience> experience;
    @NonNull Id gender;
    @NonNull List<Language> language;
}
