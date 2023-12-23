package com.resume.bot.controller;

import com.resume.hh_wrapper.ApiClient;
import com.resume.hh_wrapper.HhConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/hh")
@RequiredArgsConstructor
public class AuthController {
    private final ApiClient apiClient;

    private final HhConfig hhConfig;

    @GetMapping("/auth")
    public ModelAndView handleAuthorization(@RequestParam String code, @RequestParam String state, ModelMap modelMap) {
        String redirectLink = URLEncoder.encode("http://localhost:5000/hh/auth", StandardCharsets.UTF_8);
        System.out.println("Code: " + code);
        String result = apiClient.auth("https://hh.ru/oauth/token",
                  "grant_type=authorization_code&client_id=" + hhConfig.getClientId() +
                        "&client_secret=" + hhConfig.getClientSecret() +
                        "&redirect_uri=" + redirectLink + "&code=" + code);
        System.out.println(result); // fixme use logger
        return new ModelAndView("redirect:https://t.me/resume_gen_bot/?start=" + state, modelMap);
    }
}
