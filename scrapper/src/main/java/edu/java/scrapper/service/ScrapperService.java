package edu.java.scrapper.service;

import edu.java.scrapper.client.ApiClient;
import edu.java.scrapper.client.dto.ClientResponse;
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
