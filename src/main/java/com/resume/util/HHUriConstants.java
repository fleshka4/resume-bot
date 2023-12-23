package com.resume.util;

public interface HHUriConstants {
    String POST_CREATE_RESUME_URI = "/resumes";
    String PUT_EDIT_RESUME_URI = "/resumes/{resume_id}";
    String GET_RESUMES_URI = "/resumes/mine";
    String GET_RESUME_BY_ID_URI = "/resumes/{resume_id}";
    String GET_AREAS_URI = "/areas";
    String GET_COUNTRIES_URI = "/areas/countries";
    String GET_METROS_URI = "/metro";
    String GET_METRO_BY_CITY_URI = "/metro/{city_id}";
    String GET_INDUSTRIES_URI = "/industries";
    String GET_PROFESSIONAL_ROLES_URI = "/professional_roles";
    String GET_SKILLS_URI = "/skills";
    String GET_LOCALES_URI = "/locales";
}
