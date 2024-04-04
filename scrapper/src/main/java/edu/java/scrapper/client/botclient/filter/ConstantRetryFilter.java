package edu.java.scrapper.client.botclient.filter;

import edu.java.scrapper.configuration.ApplicationConfig;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
public class ConstantRetryFilter implements RetryFilter {
    private final ApplicationConfig applicationConfig;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        Duration delay = applicationConfig.retryPolicy().delay();
        int maxAttempts = applicationConfig.retryPolicy().maxAttempts();
        return next.exchange(request)
            .flatMap(clientResponse -> Mono.just(clientResponse)
                .filter(response -> clientResponse.statusCode().isError())
                .flatMap(response -> clientResponse.createException())
                .flatMap(Mono::error)
                .thenReturn(clientResponse))
            .retryWhen(Retry.fixedDelay(applicationConfig.retryPolicy().maxAttempts(), delay)
                .filter(throwable -> {
                    if (throwable instanceof WebClientResponseException exception) {
                        int httpCode = exception.getStatusCode().value();
                        return applicationConfig.retryPolicy().retryOnCodes().contains(httpCode);
                    }
                    return false;
                })
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure()));
    }
}
