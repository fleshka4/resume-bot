package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.Locale;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.bot.json.entity.common.Type;
import com.resume.bot.json.entity.metro.Metro;
import com.resume.bot.json.entity.roles.ProfessionalRoles;
import com.resume.hh_wrapper.impl.ApiClientImpl;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
import com.resume.util.HHUriConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HeadHunterServiceTest extends IntegrationBaseTest {
    @Autowired
    private ApiClientImpl apiClientImpl;

    @Autowired
    private HeadHunterService headHunterService;

    @Autowired
    private String hhBaseUrl;

    @Test
    public void testGetAreas() {
        List<Area> areas = headHunterService.getAreas(hhBaseUrl);
        assertNotNull(areas);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_AREAS_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String areasJson = JsonProcessor.createJsonFromEntity(areas);
        assertEquals(hhJson, areasJson);
    }

    @Test
    public void testGetCountries() {
        List<Country> countries = headHunterService.getCountries(hhBaseUrl);
        assertNotNull(countries);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_COUNTRIES_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String countriesJson = JsonProcessor.createJsonFromEntity(countries);
        assertEquals(hhJson, countriesJson);
    }

    @Test
    public void testGetMetros() {
        List<Metro> metros = headHunterService.getMetros(hhBaseUrl);
        assertNotNull(metros);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_METROS_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String metrosJson = JsonProcessor.createJsonFromEntity(metros);
        assertEquals(hhJson, metrosJson);
    }

    @Test
    public void testGetMetroByCityId() throws JSONException {
        String cityId = "1";

        Metro metro = headHunterService.getMetroByCityId(hhBaseUrl, cityId);
        assertNotNull(metro);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_METRO_BY_CITY_URI;
        String hhJson = apiClientImpl.get(hhUrl.replace("{city_id}", cityId), String.class);
        String metrosJson = JsonProcessor.createJsonFromEntity(metro);

        String modifiedMetrosJson = removeUrlFromJson(metrosJson);

        assertEquals(hhJson, modifiedMetrosJson);
    }

    @Test
    public void testGetIndustries() {
        List<Industry> industries = headHunterService.getIndustries(hhBaseUrl);
        assertNotNull(industries);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_INDUSTRIES_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String industriesJson = JsonProcessor.createJsonFromEntity(industries);
        assertEquals(hhJson, industriesJson);
    }

    @Test
    public void testGetProfessionalRoles() {
        ProfessionalRoles roles = headHunterService.getProfessionalRoles(hhBaseUrl);
        assertNotNull(roles);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_PROFESSIONAL_ROLES_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String rolesJson = JsonProcessor.createJsonFromEntity(roles);
        assertEquals(hhJson, rolesJson);
    }

    @Test
    public void testGetLocales() {
        List<Locale> locales = headHunterService.getLocales(hhBaseUrl);
        assertNotNull(locales);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_LOCALES_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String localesJson = JsonProcessor.createJsonFromEntity(locales);
        assertEquals(hhJson, localesJson);
    }

    @Test
    public void testGetLanguages() throws JSONException {
        List<Type> languages = headHunterService.getLanguages(hhBaseUrl);
        assertNotNull(languages);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_LANGUAGES_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String languagesJson = JsonProcessor.createJsonFromEntity(languages);
        String modifiedLanguagesJson = removeUidFromJson(hhJson);

        String sortedModifiedLanguagesJson = sortJsonArray(modifiedLanguagesJson);
        String sortedLanguagesJson = sortJsonArray(languagesJson);

        assertEquals(sortedModifiedLanguagesJson, sortedLanguagesJson);
    }

    private String removeUidFromJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject.remove("uid");
        }
        return jsonArray.toString();
    }

    private String removeUrlFromJson(String json) {
        return json.replaceAll("\"url\":null,", "");
    }

    private String sortJsonArray(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<JSONObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonValues.add(jsonArray.getJSONObject(i));
        }
        jsonValues.sort((o1, o2) -> {
            try {
                String id1 = o1.getString("id");
                String id2 = o2.getString("id");
                return id1.compareTo(id2);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        JSONArray sortedJsonArray = new JSONArray(jsonValues);
        return sortedJsonArray.toString();
    }
}
