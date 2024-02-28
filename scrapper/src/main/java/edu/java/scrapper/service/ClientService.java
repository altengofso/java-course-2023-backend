package edu.java.scrapper.service;

import edu.java.scrapper.client.ApiClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final List<ApiClient> apiClients;

    public ApiClient getApiClient(String link) {
        return apiClients
            .stream()
            .filter(apiClient -> apiClient.canRespond(link))
            .findFirst()
            .orElse(null);
    }
}
