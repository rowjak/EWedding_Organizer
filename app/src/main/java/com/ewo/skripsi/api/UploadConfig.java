package com.ewo.skripsi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadConfig {

//    private static String BASE_URL = "http://192.168.43.30/kinta/";
    private static String BASE_URL = "http://192.168.1.2/kinta/";

    public static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(UploadConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
