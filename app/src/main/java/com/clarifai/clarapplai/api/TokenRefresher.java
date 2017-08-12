package com.clarifai.clarapplai.api;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

final class TokenRefresher {
    private static final String LOGTAG = TokenRefresher.class.getSimpleName();

    private final OkHttpClient client;
    private final String credentials;
    private Gson gson;

    TokenRefresher(Gson gson, final String appID, final String appSecret) {
        this.client = new OkHttpClient.Builder().build();
        this.credentials = Credentials.basic(appID, appSecret);
        this.gson = gson;
    }

    Credential refresh() {
        Log.i(LOGTAG, "Refreshing credentials");
        final Request newTokenRequest = new Request.Builder()
                .url("https://api.clarifai.com/v2/token")
                .post(RequestBody.create(null, new byte[0]))
                .addHeader("Authorization", credentials)
                .build();
        try {
            final Response response = client.newCall(newTokenRequest).execute();
            final Token token = gson.fromJson(response.body().charStream(), Token.class);
            Log.i(LOGTAG, "Refreshed credentials, new access_token is " + token.access_token);
            return new Credential(token.access_token, token.expires_in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    private static class Token {
        private String access_token;
        private long expires_in;
    }
}
