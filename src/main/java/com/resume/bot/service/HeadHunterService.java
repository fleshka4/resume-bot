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
import com.resume.hh_wrapper.ApiClient;
import com.resume.util.HHUriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HeadHunterService {
    private final TokenHolderService tokenHolderService;
    private final ResumeService resumeService;
    private final ApiClient apiClient;

    public void postCreateClient(String baseUri, Client client) {
        apiClient.post(baseUri + HHUriConstants.POST_CREATE_RESUME_URI,
                JsonProcessor.createJsonFromEntity(client), String.class);
    }

    public void putEditClient(String baseUri, String resumeId, Client client) {
        apiClient.put(baseUri + HHUriConstants.PUT_EDIT_RESUME_URI.replace("{resume_id}", resumeId),
                JsonProcessor.createJsonFromEntity(client), String.class);
    }

    public List<Client> getClientsResume(String baseUri) {
        List<String> jsonStrings = apiClient.getList(baseUri + HHUriConstants.GET_RESUMES_URI, String.class);
        return jsonStrings.stream().map(json -> JsonProcessor.createEntityFromJson(json, Client.class)).collect(Collectors.toList());
    }

    public Client getClientResume(String baseUri, String resumeId) {
        return JsonProcessor.createEntityFromJson(apiClient.get(baseUri
                        + HHUriConstants.GET_RESUME_BY_ID_URI.replace("{resume_id}", resumeId), String.class), Client.class);
    }

    public List<Area> getAreas(String baseUri) {
        return apiClient.getList(baseUri + HHUriConstants.GET_AREAS_URI, Area.class);
    }

    public List<Country> getCountries(String baseUri) {
        return apiClient.getList(baseUri + HHUriConstants.GET_COUNTRIES_URI, Country.class);
    }

    public List<Metro> getMetros(String baseUri) {
        return apiClient.getList(baseUri + HHUriConstants.GET_METROS_URI, Metro.class);
    }

    public Metro getMetroByCityId(String baseUri, String cityId) {
        return apiClient.get(baseUri + HHUriConstants.GET_METRO_BY_CITY_URI.replace("{city_id}", cityId), Metro.class);
    }

    public List<Industry> getIndustries(String baseUri) {
        return apiClient.getList(baseUri + HHUriConstants.GET_INDUSTRIES_URI, Industry.class);
    }

    public ProfessionalRoles getProfessionalRoles(String baseUri) {
        return apiClient.get(baseUri + HHUriConstants.GET_PROFESSIONAL_ROLES_URI, ProfessionalRoles.class);
    }

    public Skills getSkills(String baseUri) {
        return apiClient.get(baseUri + HHUriConstants.GET_SKILLS_URI, Skills.class);
    }

    public List<Locale> getLocales(String baseUri) {
        return apiClient.getList(baseUri + HHUriConstants.GET_LOCALES_URI, Locale.class);
    }
}
