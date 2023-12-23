package com.resume.bot.controller;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Token;
import com.resume.bot.model.entity.TokenHolder;
import com.resume.bot.model.entity.User;
import com.resume.bot.service.TokenHolderService;
import com.resume.bot.service.UserService;
import com.resume.hh_wrapper.ApiClient;
import com.resume.hh_wrapper.HhConfig;
import com.resume.util.BotUtil;
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

    private final TokenHolderService holderService;

    private final UserService userService;

    @GetMapping("/auth")
    public ModelAndView handleAuthorization(@RequestParam String code, @RequestParam String state, ModelMap modelMap) {
        String redirectLink = URLEncoder.encode("http://localhost:5000/hh/auth", StandardCharsets.UTF_8);
        System.out.println("Code: " + code);
        String result = apiClient.auth("https://hh.ru/oauth/token",
                "grant_type=authorization_code&client_id=" + hhConfig.getClientId() +
                        "&client_secret=" + hhConfig.getClientSecret() +
                        "&redirect_uri=" + redirectLink + "&code=" + code);

        Token token = JsonProcessor.createEntityFromJson(result, Token.class);
        Long stateNum = Long.parseLong(state);

        User user = userService.getUser(BotUtil.states.get(stateNum)); // todo: logger + handle invalid urls
        holderService.save(new TokenHolder(token, user));
        System.out.println(token); // fixme use logger
        return new ModelAndView("redirect:https://t.me/resume_gen_bot/?start=" + state, modelMap);
    }
}
