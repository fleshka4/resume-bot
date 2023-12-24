package com.resume.bot.service;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.Locale;
import com.resume.bot.json.entity.Skills;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.client.Resumes;
import com.resume.bot.json.entity.common.Type;
import com.resume.bot.json.entity.metro.Metro;
import com.resume.bot.json.entity.roles.ProfessionalRoles;
import com.resume.hh_wrapper.impl.ApiClientImpl;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
import com.resume.util.HHUriConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class HeadHunterService {
    private final ApiClientImpl apiClientImpl;
    private final ApiClientTokenImpl apiClientTokenImpl;
    private final UserService userService;

    // region: AUTHORIZED REQUESTS

    public void postCreateClient(String baseUri, Long tgUid, Resume resume) {
        apiClientTokenImpl.post(baseUri + HHUriConstants.POST_CREATE_RESUME_URI,
                userService.getUser(tgUid).getTokenHolder().getAccessToken(),
                resume, Resume.class);
    }

    public void putEditClient(String baseUri, Long tgUid, String resumeId, Resume resume) {
        apiClientTokenImpl.put(baseUri + HHUriConstants.PUT_EDIT_RESUME_URI.replace("{resume_id}", resumeId),
                userService.getUser(tgUid).getTokenHolder().getAccessToken(),
                JsonProcessor.createJsonFromEntity(resume), String.class);
    }

    public List<Resume> getClientResumes(String baseUri, Long tgUid) {
        Resumes resumes = apiClientTokenImpl.get(baseUri + HHUriConstants.GET_RESUMES_URI,
                userService.getUser(tgUid).getTokenHolder().getAccessToken(), Resumes.class);
        return resumes.getItems();
    }

    public Resume getClientResume(String baseUri, Long tgUid, String resumeId) {
        return JsonProcessor.createEntityFromJson(
                apiClientTokenImpl.get(baseUri + HHUriConstants.GET_RESUME_BY_ID_URI.replace("{resume_id}", resumeId),
                        userService.getUser(tgUid).getTokenHolder().getAccessToken(), String.class), Resume.class);
    }

    // endregion

    // region: UNAUTHORIZED REQUESTS

    public List<Area> getAreas(String baseUri) {
        return apiClientImpl.getList(baseUri + HHUriConstants.GET_AREAS_URI, Area.class);
    }

    public List<Country> getCountries(String baseUri) {
        return apiClientImpl.getList(baseUri + HHUriConstants.GET_COUNTRIES_URI, Country.class);
    }

    public List<Metro> getMetros(String baseUri) {
        return apiClientImpl.getList(baseUri + HHUriConstants.GET_METROS_URI, Metro.class);
    }

    public List<Industry> getIndustries(String baseUri) {
        return apiClientImpl.getList(baseUri + HHUriConstants.GET_INDUSTRIES_URI, Industry.class);
    }

    public ProfessionalRoles getProfessionalRoles(String baseUri) {
        return apiClientImpl.get(baseUri + HHUriConstants.GET_PROFESSIONAL_ROLES_URI, ProfessionalRoles.class);
    }

    public Skills getSkills(String baseUri) {
        return apiClientImpl.get(baseUri + HHUriConstants.GET_SKILLS_URI, Skills.class);
    }

    public List<Locale> getLocales(String baseUri) {
        return apiClientImpl.getList(baseUri + HHUriConstants.GET_LOCALES_URI, Locale.class);
    }

    public List<Type> getLanguages(String baseUri) {
        return apiClientImpl.getList(baseUri + HHUriConstants.GET_LANGUAGES_URI, Type.class);
    }

    public Metro getMetroByCityId(String baseUri, String cityId) {
        return apiClientImpl.get(baseUri + HHUriConstants.GET_METRO_BY_CITY_URI.replace("{city_id}", cityId), Metro.class);
    }

    // endregion
}
