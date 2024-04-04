package edu.java.scrapper.client.botclient.filter;

import edu.java.scrapper.configuration.ApplicationConfig;
import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
public class LinearRetryFilter implements RetryFilter {
    private final ApplicationConfig applicationConfig;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        int maxAttempts = applicationConfig.retryPolicy().maxAttempts();
        Duration delay = applicationConfig.retryPolicy().delay();
        LinearRetry linearRetry = getLinearRetry(maxAttempts, delay);
        return next.exchange(request)
            .flatMap(clientResponse -> Mono.just(clientResponse)
                .filter(response -> clientResponse.statusCode().isError())
                .flatMap(response -> clientResponse.createException())
                .flatMap(Mono::error)
                .thenReturn(clientResponse))
            .retryWhen(linearRetry);
    }

    private LinearRetry getLinearRetry(int maxAttempts, Duration delay) {
        return new LinearRetry(
            maxAttempts,
            delay,
            throwable -> {
                if (throwable instanceof WebClientResponseException exception) {
                    int httpCode = exception.getStatusCode().value();
                    return applicationConfig.retryPolicy().retryOnCodes().contains(httpCode);
                }
                return false;
            },
            (retryBackoffSpec, retrySignal) -> retrySignal.failure()
        );
    }

    @RequiredArgsConstructor
    private class LinearRetry extends Retry {
        private final int maxAttempts;
        private final Duration delay;
        private final Predicate<Throwable> filter;
        private final BiFunction<LinearRetry, RetrySignal, Throwable> retryExhaustedGenerator;

        @Override
        public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
            return flux.flatMap(rs -> {
                RetrySignal copy = rs.copy();
                if (!filter.test(rs.failure())) {
                    return Mono.error(rs.failure());
                }
                if (rs.totalRetries() < maxAttempts) {
                    return Mono.delay(delay).thenReturn(rs.totalRetries());
                } else {
                    return Mono.error(retryExhaustedGenerator.apply(this, copy));
                }
            });
        }
    }
}
