package edu.java.scrapper.app.client;

import edu.java.scrapper.app.client.dto.ClientResponse;

public interface ApiClient {
    boolean canAccess(String link);

    ClientResponse getResponse(String link);
}
