package com.unit.resume.bot.service;

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
import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.TokenHolderService;
import com.resume.bot.service.UserService;
import com.resume.hh_wrapper.impl.ApiClientImpl;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HeadHunterServiceTest {

    @Mock
    private ApiClientImpl apiClientImpl;

    @Mock
    private TokenHolderService tokenHolderService;

    @Mock
    private ApiClientTokenImpl apiClientTokenImpl;

    @Mock
    private UserService userService;

    @InjectMocks
    private HeadHunterService headHunterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPostCreateClient() {
        String baseUri = "https://example.ru";
        Long tgUid = 123L;
        String accessToken = "testAccessToken";
        String expectedLocation = "https://example.ru/resumes/1";
        Resume resume = new Resume();

        TokenHolder tokenHolder = new TokenHolder();
        tokenHolder.setAccessToken(accessToken);
        User user = new User();
        user.setTokenHolder(tokenHolder);

        when(userService.getUser(tgUid)).thenReturn(user);
        when(apiClientTokenImpl.post(anyString(), anyString(), any(Resume.class), eq(Resume.class)))
                .thenReturn(ResponseEntity.created(URI.create("https://example.ru/resumes/1")).build());

        String location = headHunterService.postCreateClient(baseUri, tgUid, resume);

        assertEquals(expectedLocation, location);
        verify(apiClientTokenImpl).post(eq("https://example.ru/resumes"), eq(accessToken), eq(resume), eq(Resume.class));
    }

    @Test
    public void testPutEditClient() {
        String baseUri = "https://example.ru";
        Long tgUid = 123L;
        String resumeId = "1";
        Resume resume = new Resume();
        String accessToken = "testAccessToken";

        TokenHolder tokenHolder = new TokenHolder();
        tokenHolder.setAccessToken(accessToken);
        User user = new User();
        user.setTokenHolder(tokenHolder);
        when(userService.getUser(tgUid)).thenReturn(user);

        ResponseEntity<String> expectedResponse = ResponseEntity.ok().build();
        when(apiClientTokenImpl.put(anyString(), anyString(), anyString(), eq(String.class)))
                .thenReturn(String.valueOf(expectedResponse));

        headHunterService.putEditClient(baseUri, tgUid, resumeId, resume);
        verify(apiClientTokenImpl).put(eq("https://example.ru/resumes/1"), eq(accessToken),
                anyString(), eq(String.class));
    }

    @Test
    public void testGetClientResumes() {
        String baseUri = "https://example.ru";
        Long tgUid = 123L;
        String accessToken = "testAccessToken";

        TokenHolder tokenHolder = new TokenHolder();
        tokenHolder.setAccessToken(accessToken);
        User user = new User();
        user.setTokenHolder(tokenHolder);
        when(userService.getUser(tgUid)).thenReturn(user);

        Resumes mockResumes = new Resumes();
        Resume resume1 = new Resume();
        resume1.setTitle("Resume 1");
        Resume resume2 = new Resume();
        resume2.setTitle("Resume 2");
        mockResumes.setItems(Arrays.asList(resume1, resume2));

        when(apiClientTokenImpl.get(anyString(), eq(accessToken), eq(Resumes.class)))
                .thenReturn(mockResumes);

        List<Resume> resumes = headHunterService.getClientResumes(baseUri, tgUid);

        assertEquals(mockResumes.getItems(), resumes);
        verify(apiClientTokenImpl).get(eq("https://example.ru/resumes/mine"), eq(accessToken), eq(Resumes.class));
    }

    @Test
    public void testGetClientResume() {
        String baseUri = "https://example.ru";
        Long tgUid = 123L;
        String accessToken = "testAccessToken";
        String resumeId = "1";

        TokenHolder tokenHolder = new TokenHolder();
        tokenHolder.setAccessToken(accessToken);
        User user = new User();
        user.setTokenHolder(tokenHolder);
        when(userService.getUser(tgUid)).thenReturn(user);

        String jsonResponse = "{\"title\":\"John Doe Resume\"}";
        when(apiClientTokenImpl.get(anyString(), eq(accessToken), eq(String.class)))
                .thenReturn(jsonResponse);

        Resume resume = headHunterService.getClientResume(baseUri, tgUid, resumeId);

        assertEquals("John Doe Resume", resume.getTitle()); // Проверяем, что название резюме правильное
        verify(apiClientTokenImpl).get(eq("https://example.ru/resumes/1"), eq(accessToken), eq(String.class));
    }

    @Test
    public void testGetAreas() {
        String baseUri = "https://example.ru";

        List<Area> mockAreas = Arrays.asList(new Area(), new Area());
        when(apiClientImpl.getList(anyString(), eq(Area.class))).thenReturn(mockAreas);

        List<Area> areas = headHunterService.getAreas(baseUri);

        assertEquals(mockAreas, areas);
        verify(apiClientImpl).getList(eq("https://example.ru/areas"), eq(Area.class));
    }

    @Test
    public void testGetCountries() {
        String baseUri = "https://example.ru";

        List<Country> mockCountries = Arrays.asList(new Country(), new Country());
        when(apiClientImpl.getList(anyString(), eq(Country.class))).thenReturn(mockCountries);

        List<Country> countries = headHunterService.getCountries(baseUri);

        assertEquals(mockCountries, countries);
        verify(apiClientImpl).getList(eq("https://example.ru/areas/countries"), eq(Country.class));
    }

    @Test
    public void testGetMetros() {
        String baseUri = "https://example.ru";

        List<Metro> mockMetros = Arrays.asList(new Metro(), new Metro());
        when(apiClientImpl.getList(anyString(), eq(Metro.class))).thenReturn(mockMetros);

        List<Metro> metros = headHunterService.getMetros(baseUri);

        assertEquals(mockMetros, metros);
        verify(apiClientImpl).getList(eq("https://example.ru/metro"), eq(Metro.class));
    }

    @Test
    public void testGetMetroByCityId() {
        String baseUri = "https://example.ru";
        String cityId = "1";

        Metro mockMetro = new Metro();
        when(apiClientImpl.get(anyString(), eq(Metro.class))).thenReturn(mockMetro);

        Metro metro = headHunterService.getMetroByCityId(baseUri, cityId);

        assertEquals(mockMetro, metro);
        verify(apiClientImpl).get(eq("https://example.ru/metro/1"), eq(Metro.class));
    }

    @Test
    public void testGetIndustries() {
        String baseUri = "https://example.ru";

        List<Industry> mockIndustries = Arrays.asList(new Industry(), new Industry());
        when(apiClientImpl.getList(anyString(), eq(Industry.class))).thenReturn(mockIndustries);

        List<Industry> industries = headHunterService.getIndustries(baseUri);

        assertEquals(mockIndustries, industries);
        verify(apiClientImpl).getList(eq("https://example.ru/industries"), eq(Industry.class));
    }

    @Test
    public void testGetProfessionalRoles() {
        String baseUri = "https://example.ru";

        ProfessionalRoles mockProfessionalRoles = new ProfessionalRoles();
        when(apiClientImpl.get(anyString(), eq(ProfessionalRoles.class))).thenReturn(mockProfessionalRoles);

        ProfessionalRoles professionalRoles = headHunterService.getProfessionalRoles(baseUri);

        assertEquals(mockProfessionalRoles, professionalRoles);
        verify(apiClientImpl).get(eq("https://example.ru/professional_roles"), eq(ProfessionalRoles.class));
    }

    @Test
    public void testGetSkills() {
        String baseUri = "https://example.ru";

        Skills mockSkills = new Skills();
        when(apiClientImpl.get(anyString(), eq(Skills.class))).thenReturn(mockSkills);

        Skills skills = headHunterService.getSkills(baseUri);

        assertEquals(mockSkills, skills);
        verify(apiClientImpl).get(eq("https://example.ru/skills"), eq(Skills.class));
    }

    @Test
    public void testGetLocales() {
        String baseUri = "https://example.ru";

        List<Locale> mockLocales = Arrays.asList(new Locale(), new Locale());
        when(apiClientImpl.getList(anyString(), eq(Locale.class))).thenReturn(mockLocales);

        List<Locale> locales = headHunterService.getLocales(baseUri);

        assertEquals(mockLocales, locales);
        verify(apiClientImpl).getList(eq("https://example.ru/locales"), eq(Locale.class));
    }

    @Test
    public void testGetLanguages() {
        String baseUri = "https://example.ru";

        List<Type> mockLanguages = Arrays.asList(new Type(), new Type());
        when(apiClientImpl.getList(anyString(), eq(Type.class))).thenReturn(mockLanguages);

        List<Type> languages = headHunterService.getLanguages(baseUri);

        assertEquals(mockLanguages, languages);
        verify(apiClientImpl).getList(eq("https://example.ru/languages"), eq(Type.class));
    }
}
