package com.sptech.school.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    private final String site;
    private final String authHeader ;
    private final HttpClient http;
    private final Duration timeout = Duration.ofSeconds(20);

    public Client(String site, String email, String apiToken) {
        String s = firstNonNull(site, System.getenv("JIRA_URL"));
        String e = firstNonNull(email, System.getenv("JIRA_EMAIL"));
        String t = firstNonNull(apiToken, System.getenv("JIRA_API_TOKEN")); // NENHUM token no código!

        if (s == null || s.isBlank()) throw new IllegalArgumentException("Url não informado (configure a variável JIRA_URL)");
        if (e == null || e.isBlank()) throw new IllegalArgumentException("EMAIL não informado (configure JIRA_EMAIL)");
        if (t == null || t.isBlank()) throw new IllegalArgumentException("TOKEN não informado (configure JIRA_API_TOKEN)");

        this.site = s.endsWith("/") ? s.substring(0, s.length() - 1) : s;

        String credential = e + ":" + t;
        String encoded = Base64.getEncoder().encodeToString(credential.getBytes(StandardCharsets.UTF_8));
        this.authHeader = "Basic " + encoded;

        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    private static String firstNonNull(String a, String b) {
        return (a != null && !a.isBlank()) ? a : b;
    }

    public HttpResponse<String> postJson(String caminhoPath, String json) throws Exception {
        if (caminhoPath == null || !caminhoPath.startsWith("/")) {
            throw new IllegalArgumentException("caminhoPath deve começar com '/'");
        }
        String url = site + caminhoPath;

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = http.send(
                req,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );
        return response;
    }


}