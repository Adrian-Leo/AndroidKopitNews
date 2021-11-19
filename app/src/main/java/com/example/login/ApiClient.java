package com.example.login;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL="https://newsapi.org/v2/";
    private static com.example.login.ApiClient apiClient;
    private static Retrofit retrofit;

    private ApiClient(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized com.example.login.ApiClient getInstance(){
         if(apiClient==null);{
             apiClient = new com.example.login.ApiClient();
        }
        return apiClient;
    }

    public apiInterface getApi(){
        return retrofit.create(apiInterface.class);
    }
}
