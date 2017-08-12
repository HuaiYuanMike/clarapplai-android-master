package com.clarifai.clarapplai.api;

final class Credential {
    private final String accessToken;
    private final long expiresAt;

    Credential(String accessToken, long expiresInMillis) {
        this.accessToken = accessToken;
        this.expiresAt = System.currentTimeMillis() + expiresInMillis;
    }

    String getAccessToken() {
        return accessToken;
    }

    long getExpiresAt() {
        return expiresAt;
    }
}
