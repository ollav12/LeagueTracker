package com.leaguetracker.app.service.riot;

import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.WebClientConfig;
import com.leaguetracker.app.service.riot.endpoint.RiotEndpoint;

public class RiotRequest<T> {
    private static final int MAX_RETRIES = 3;
    private static final int RATE_LIMIT_STATUS = 429;

    private final WebClientConfig webClientConfig;
    private final WebClient webClient;
    private final RiotEndpoint endpoint;
    private final String region;
    private final ObjectMapper mapper;
    private final Class<T> responseType;
    private final Object[] params;

    public RiotRequest(
            RiotEndpoint endpoint,
            String region,
            ObjectMapper mapper,
            Class<T> responseType,
            WebClientConfig webClientConfig,
            WebClient webClient,
            Object... params) {
        this.endpoint = endpoint;
        this.region = region;
        this.mapper = mapper;
        this.responseType = responseType;
        this.webClientConfig = webClientConfig;
        this.webClient = webClient;
        this.params = params;
    }

    public T execute() {
        return webClient.get()
                .uri(webClientConfig.createUrl(endpoint, region, params))
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
                    try {
                        return mapper.readValue(response, responseType);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
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
