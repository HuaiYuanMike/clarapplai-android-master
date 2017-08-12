package com.clarifai.clarapplai.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClarifaiEmbeddingRequest {
    private final Gson gson;
    private final Request request;
    private final OkHttpClient client;

    ClarifaiEmbeddingRequest(Gson gson, OkHttpClient client, Request request) {
        this.gson = gson;
        this.client = client;
        this.request = request;
    }

    public ClarifaiEmbeddingResult requestSync() throws IOException {
        return parseResponse(client.newCall(request).execute());
    }

    public void requestAsync(final Callback callback) {
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onNetworkFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(parseResponse(response));
                } else {
                    callback.onUnsuccessfulResponse(response);
                }
            }
        });
    }

    private ClarifaiEmbeddingResult parseResponse(Response response) {
        final JsonArray embedJsonArray =
                gson.fromJson(response.body().charStream(), JsonObject.class)
                        .getAsJsonObject()
                        .getAsJsonArray("results")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("result")
                        .getAsJsonArray("embed");
        final double[] embeddings = new double[embedJsonArray.size()];
        for (int i = 0; i < embedJsonArray.size(); i++) {
            embeddings[i] = embedJsonArray.get(i).getAsDouble();
        }
        return new ClarifaiEmbeddingResult(embeddings);
    }

    public interface Callback {
        /**
         * Called when the server responds with a 2xx HTTP status code.
         *
         * @param result The parsed-out embeddings
         */
        void onSuccess(ClarifaiEmbeddingResult result);

        /**
         * Called when the request was received by the server successfully, but the server responded
         * with an unhappy HTTP status code, like a 400 or 500.
         *
         * @param response The response from the server
         */
        void onUnsuccessfulResponse(Response response);

        /**
         * Called when the server didn't get to respond, due to a timeout, network connectivity
         * error, etc.
         *
         * @param exception An exception explaining the specific issue in connectivity.
         */
        void onNetworkFailure(IOException exception);
    }
}
