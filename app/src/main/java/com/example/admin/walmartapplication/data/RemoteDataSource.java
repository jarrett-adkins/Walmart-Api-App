package com.example.admin.walmartapplication.data;

import com.example.admin.walmartapplication.model.WalmartLookup;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource {

    public static final String BASE_URL = "http://api.walmartlabs.com/v1/";

    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                .build();

        return retrofit;
    }

    public static Observable<WalmartLookup> getWalmartLookup( String query, int start ) {
        Retrofit retrofit = create();
        RemoteService remoteService = retrofit.create( RemoteService.class );

        return remoteService.getWalmartLookup( query, start );
    }
}
//http://api.walmartlabs.com/v1/search?query=ipod&format=json&apiKey=