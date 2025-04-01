package com.leaguetracker.app.service.riot;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.util.function.Function;

public class RiotRequest<T> {
    private static final int MAX_RETRIES = 3;
    private static final int RATE_LIMIT_STATUS = 429;

    private final WebClient webClient;
    private final String url;
    private final Function<String, T> responseMapper;

    public RiotRequest(String region, String endpoint, String apiKey, Function<String, T> responseMapper) {
        this.url = "https://" + region + ".api.riotgames.com/" + endpoint + apiKey;
        this.webClient = WebClient.create();
        this.responseMapper = responseMapper;
    }

    public T execute() {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(MAX_RETRIES, Duration.ofSeconds(1))
                        .filter(this::shouldRetry)
                        .doBeforeRetry(retrySignal -> {
                            System.out.println("Retrying request after failure. Attempt: " +
                                    (retrySignal.totalRetries() + 1));
                            handleRateLimit(retrySignal.failure());
                        }))
                .map(response -> {
                    System.out.println("Response Recieve");
                    return responseMapper.apply(response);
                })
                .block();
    }

    private boolean shouldRetry(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException ex = (WebClientResponseException) throwable;
            return ex.getStatusCode().value() == RATE_LIMIT_STATUS;
        }
        return false;
    }

    private void handleRateLimit(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException ex = (WebClientResponseException) throwable;
            if (ex.getStatusCode().value() == RATE_LIMIT_STATUS) {
                String retryAfter = ex.getHeaders().getFirst("Retry-After");
                String appRateLimit = ex.getHeaders().getFirst("X-App-Rate-Limit");
                String appRateLimitCount = ex.getHeaders().getFirst("X-App-Rate-Limit-Count");

                System.out.println("Rate limit exceeded. App Rate Limit: " + appRateLimit +
                        ", Current Count: " + appRateLimitCount);

                if (retryAfter != null) {
                    long sleepTime = Long.parseLong(retryAfter) * 1000;
                    try {
                        System.out.println("Waiting for " + (sleepTime / 1000) + " seconds before retry");
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Rate limit sleep interrupted", ie);
                    }
                }
            }
        }
    }
}
