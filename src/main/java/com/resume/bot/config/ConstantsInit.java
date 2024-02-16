package com.resume.bot.config;

import com.resume.bot.service.HeadHunterService;
import com.resume.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConstantsInit {
    private final HeadHunterService headHunterService;
    private final String hhBaseUrl;

    @EventListener({ApplicationReadyEvent.class})
    public void initAreas() {
        Constants.AREAS = headHunterService.getAreas(hhBaseUrl);
        log.info("Areas initialized");
    }

    @EventListener({ApplicationReadyEvent.class})
    public void initCountries() {
        Constants.COUNTRIES = headHunterService.getCountries(hhBaseUrl);
        log.info("Countries initialized");
    }

    @EventListener({ApplicationReadyEvent.class})
    public void initMetros() {
        Constants.METROS = headHunterService.getMetros(hhBaseUrl);
        log.info("Metros initialized");
    }

    @EventListener({ApplicationReadyEvent.class})
    public void initIndustries() {
        Constants.INDUSTRIES = headHunterService.getIndustries(hhBaseUrl);
        Constants.INDUSTRIES_MAP = new HashMap<>();
        for (int i = 0; i < Constants.INDUSTRIES.size(); i++) {
            Constants.INDUSTRIES_MAP.put(i, Constants.INDUSTRIES.get(i));
        }
        log.info("Industries initialized");
    }

    @EventListener({ApplicationReadyEvent.class})
    public void initProfessionalRoles() {
        Constants.PROFESSIONAL_ROLES = headHunterService.getProfessionalRoles(hhBaseUrl);
        log.info("Professional roles initialized");
    }
//
//    @EventListener({ApplicationReadyEvent.class})
//    public void initSkills() {
//        Constants.SKILLS = headHunterService.getSkills(hhBaseUrl);
//        log.info("Skills initialized");
//    }

    @EventListener({ApplicationReadyEvent.class})
    public void initLocales() {
        Constants.LOCALES = headHunterService.getLocales(hhBaseUrl);
        log.info("Locales initialized");
    }

    @EventListener({ApplicationReadyEvent.class})
    public void initLanguages() {
        Constants.LANGUAGES = headHunterService.getLanguages(hhBaseUrl);
        log.info("Languages initialized");
    }
}
