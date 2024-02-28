package edu.java.scrapper.client;

import edu.java.scrapper.client.dto.ClientResponse;

public interface ApiClient {
    boolean canRespond(String link);

    ClientResponse getResponse(String link);
}
