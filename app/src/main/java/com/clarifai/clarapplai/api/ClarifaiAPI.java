package com.clarifai.clarapplai.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class ClarifaiAPI {
    private static final String LOGTAG = ClarifaiAPI.class.getSimpleName();

    private Credential currentCredential = null;

    private final TokenRefresher refresher;
    private final OkHttpClient client;
    private final ClarifaiRetrofitService service;
    private final Gson gson;

    public ClarifaiAPI(String appID, String appSecret) {
        if (appID == null) {
            throw new IllegalArgumentException("appID == null");
        }
        if (appSecret == null) {
            throw new IllegalArgumentException("appSecret == null");
        }
        this.client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final String token = getCredential().getAccessToken();
                        final Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();
                        Log.i(LOGTAG,
                                "Clarifai request being made to "
                                + request.url()
                                + " with token "
                                + token);
                        return chain.proceed(request);
                    }
                }).build();
        this.gson = new Gson();
        this.refresher = new TokenRefresher(gson, appID, appSecret);

        service = new Retrofit.Builder()
                .baseUrl("https://api.clarifai.com/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(ClarifaiRetrofitService.class);

        // Get an access token right now, to pay the cost up-front
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    currentCredential = refresher.refresh();
                    return null;
                }
            }.execute();
        } else {
            currentCredential = refresher.refresh();
        }
    }

    public ClarifaiEmbeddingRequest requestEmbeddings(byte[] imageBytes) {
        return new ClarifaiEmbeddingRequest(gson, client, getCall(imageBytes).request());
    }

    public ClarifaiEmbeddingRequest requestEmbeddings(Bitmap bitmap) {
        return requestEmbeddings(bitmapToBytes(bitmap));
    }

    private Call<ClarifaiEmbeddingResult> getCall(byte[] imageBytes) {
        return service.embed(
                RequestBody.create(MediaType.parse("text/*"),
                        "face-v1.2"),
                RequestBody.create(MediaType.parse("image/*"),
                        Base64.encodeToString(imageBytes, Base64.DEFAULT)));
    }

    private synchronized Credential getCredential() {
        if (System.currentTimeMillis() >= currentCredential.getExpiresAt() - 60000) {
            currentCredential = refresher.refresh();
        }
        return currentCredential;

    }

    private static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    Log.e(LOGTAG, "Error while trying to close BAOS for Bitmap -> byte[] conversion", e);
                }
            }
        }
    }

}
