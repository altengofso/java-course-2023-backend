package edu.java.scrapper.client.apiclient;

import edu.java.scrapper.client.dto.ClientResponse;

public interface ApiClient {
    boolean canAccess(String link);

    ClientResponse getResponse(String link);
}
