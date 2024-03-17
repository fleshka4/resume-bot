package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.config.ApplicationConfig;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.Locale;
import com.resume.bot.json.entity.Skills;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.bot.json.entity.client.Language;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.client.Resumes;
import com.resume.bot.json.entity.common.Type;
import com.resume.bot.json.entity.metro.Metro;
import com.resume.bot.json.entity.roles.ProfessionalRoles;
import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.TokenHolderService;
import com.resume.bot.service.UserService;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.hh_wrapper.impl.ApiClientImpl;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
import com.resume.util.HHUriConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HeadHunterServiceTest extends IntegrationBaseTest {
    @Autowired
    private ApiClientImpl apiClientImpl;

    @Autowired
    private ApiClientTokenImpl apiClientTokenImpl;

    @Autowired
    private UserService userService;

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
    public void testGetMetroByCityId() {
        String cityId = "1";

        Metro metro = headHunterService.getMetroByCityId(hhBaseUrl, cityId);
        assertNotNull(metro);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_METRO_BY_CITY_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String metrosJson = JsonProcessor.createJsonFromEntity(metro);
        assertEquals(hhJson, metrosJson);
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
    public void testGetLanguages() {
        List<Type> languages = headHunterService.getLanguages(hhBaseUrl);
        assertNotNull(languages);

        String hhUrl = hhBaseUrl + HHUriConstants.GET_LANGUAGES_URI;
        String hhJson = apiClientImpl.get(hhUrl, String.class);
        String languagesJson = JsonProcessor.createJsonFromEntity(languages);
        assertEquals(hhJson, languagesJson);
    }
}
