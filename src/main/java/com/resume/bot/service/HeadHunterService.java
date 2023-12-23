package com.resume.bot.service;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.client.Client;
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
}
