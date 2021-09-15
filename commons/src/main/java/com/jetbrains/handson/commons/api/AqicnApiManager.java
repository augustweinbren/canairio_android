package com.jetbrains.handson.commons.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

import java.io.FileNotFoundException;
import java.util.logging.Logger;
import java.io.File;

/**
 * Created by Antonio Vanegas @hpsaturn on 12/31/19.
 */
public class AqicnApiManager {

    public static String TAG = AqicnApiManager.class.getSimpleName();
    public static final String API_URL = "https://api.waqi.info/";
    private static final boolean DEBUG = false;
    private static final Logger LOGGER = Logger.getLogger(
            AqicnApiManager.class.getSimpleName() );

    private static AqicnApiManager instance;
    private AqicnInterface service;
    private static String API_KEY;

    public static AqicnApiManager getInstance() {
        if (instance == null) {
            instance = new AqicnApiManager();
        }
        return instance;
    }

    public void init() {
        try {
            // Reading XML as String using jCabi library
            XML xml = new XMLDocument(new File("src/main/res/values/api_aqicn.xml"));
            String xmlString = xml.toString();
            API_KEY = xmlString.split("\"api_aqicn_key\">")[1].split("</")[0];
        } catch (FileNotFoundException e) {
            LOGGER.severe("api_aqicn_key.xml not found");
        }
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        LOGGER.info("[API] AQICN retrofit builder set to "+API_URL);
        Retrofit retrofit = new Retrofit.
                Builder().
                addConverterFactory(GsonConverterFactory.create(gson)).
                baseUrl(API_URL).
                build();

        service = retrofit.create(AqicnInterface.class);
    }

    public void getDataFromCity(String city, Callback<AqicnDataResponse> callback){
        Call<AqicnDataResponse> call = service.getDataFromCity(API_KEY,city);
        call.enqueue(callback);
    }

    public void getDataFromHere(Callback<AqicnDataResponse> callback){
        Call<AqicnDataResponse> call = service.getDataFromHere(API_KEY);
        call.enqueue(callback);
    }

    public void getDataFromMapBounds(String latlng, Callback<AqicnDataResponse> callback){
        Call<AqicnDataResponse> call = service.getDataFromMapBounds(API_KEY,latlng);
        call.enqueue(callback);
    }

}
