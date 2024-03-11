package edu.java.bot.scrapperclient.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Log4j2
public class ErrorLoggingFilter implements ExchangeFilterFunction {
    private static final String ERROR_RESPONSE = "Error response from server: {}";

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
            .doOnNext(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    clientResponse.bodyToMono(String.class).subscribe(error -> log.error(ERROR_RESPONSE, error));
                }
            });
    }
}
