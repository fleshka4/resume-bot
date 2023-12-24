package com.resume.bot.service;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.Locale;
import com.resume.bot.json.entity.Skills;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.bot.json.entity.client.Client;
import com.resume.bot.json.entity.metro.Metro;
import com.resume.bot.json.entity.roles.ProfessionalRoles;
import com.resume.bot.model.entity.User;
import com.resume.hh_wrapper.impl.ApiClientImpl;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
import com.resume.util.HHUriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HeadHunterService {
    private final ApiClientImpl apiClientImpl;
    private final ApiClientTokenImpl apiClientTokenImpl;
    private final UserService userService;

    // region: AUTHORIZED REQUESTS

    public void postCreateClient(String baseUri, Long tgUid, Client client) {
        apiClientTokenImpl.post(baseUri + HHUriConstants.POST_CREATE_RESUME_URI,
                userService.getUser(tgUid).getTokenHolder().getAccessToken(),
                JsonProcessor.createJsonFromEntity(client), String.class);
    }

    public void putEditClient(String baseUri, Long tgUid, String resumeId, Client client) {
        apiClientTokenImpl.put(baseUri + HHUriConstants.PUT_EDIT_RESUME_URI.replace("{resume_id}", resumeId),
                userService.getUser(tgUid).getTokenHolder().getAccessToken(),
                JsonProcessor.createJsonFromEntity(client), String.class);
    }

    public List<Client> getClientsResume(String baseUri, Long tgUid) {
        List<String> jsonStrings = apiClientTokenImpl.getList(baseUri + HHUriConstants.GET_RESUMES_URI,
                userService.getUser(tgUid).getTokenHolder().getAccessToken(), String.class);
        return jsonStrings.stream().map(json -> JsonProcessor.createEntityFromJson(json, Client.class)).collect(Collectors.toList());
    }

    public Client getClientResume(String baseUri, Long tgUid, String resumeId) {
        return JsonProcessor.createEntityFromJson(
                apiClientTokenImpl.get(baseUri + HHUriConstants.GET_RESUME_BY_ID_URI.replace("{resume_id}", resumeId),
                        userService.getUser(tgUid).getTokenHolder().getAccessToken(), String.class), Client.class);
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

    public Metro getMetroByCityId(String baseUri, String cityId) {
        return apiClientImpl.get(baseUri + HHUriConstants.GET_METRO_BY_CITY_URI.replace("{city_id}", cityId), Metro.class);
    }

    // endregion
}
