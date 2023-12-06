package com.resume.bot.service;

import com.resume.bot.json.entity.Client;
import com.resume.hh_wrapper.ApiClientImpl;
import com.resume.util.HHUriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HeadHunterService {
    private TokenHolderService tokenHolderService;
    private ResumeService resumeService;
    private ApiClientImpl apiClient;

    /*public Metro getMetro(int id) {
        return get("metro/" + id, Metro.class);
    }*/

    public void postCreateClient(String baseUri, Client client) {
        apiClient.post(baseUri + HHUriConstants.POST_CREATE_RESUME_URI, client, Client.class);
    }

    public void putEditClient(String baseUri, String resumeId, Client client) {
        apiClient.put(baseUri + HHUriConstants.PUT_EDIT_RESUME_URI.replace("{resume_id}", resumeId), client, Client.class);
    }

    public List<Client> getClientsResume(String baseUri) {
        return apiClient.getList(baseUri + HHUriConstants.GET_RESUMES_URI, Client.class);
    }

    public Client getClientResume(String baseUri, String resumeId) {
        return apiClient.get(baseUri + HHUriConstants.GET_RESUME_BY_ID_URI.replace("{resume_id}", resumeId), Client.class);
    }
}
