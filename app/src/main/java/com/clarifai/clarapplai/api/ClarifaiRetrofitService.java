package com.clarifai.clarapplai.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

interface ClarifaiRetrofitService {
    @Multipart
    @POST("embed")
    Call<ClarifaiEmbeddingResult> embed(@Part("model") RequestBody model, @Part("encoded_data") RequestBody encodedData);
}
