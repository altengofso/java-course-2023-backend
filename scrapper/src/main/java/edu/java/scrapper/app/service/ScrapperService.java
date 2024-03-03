package edu.java.scrapper.app.service;

import edu.java.scrapper.app.client.ApiClient;
import edu.java.scrapper.app.client.dto.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperService {
    private final ClientService clientService;

    public ClientResponse getUpdatedAt(String link) {
        ApiClient apiClient = clientService.getApiClient(link);
        return apiClient.getResponse(link);
    }
}
