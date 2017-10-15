package com.example.admin.walmartapplication.data;

import com.example.admin.walmartapplication.model.WalmartLookup;
import com.example.admin.walmartapplication.utils.Constants;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteService {

    @GET("search?format=json&numItems=25&apiKey=" + Constants.VALUES.API_KEY)
    Observable<WalmartLookup> getWalmartLookup( @Query("query") String query );

}

/*
Paginated

Get all items in Electronics on rollback in JSON format

http://api.walmartlabs.com/v1/paginated/items?category=3944&specialOffer=rollback&apiKey={apiKey}
&lsPublisherId={Your LinkShare Publisher Id}&format=json

Get all items for a brand "foo"

http://api.walmartlabs.com/v1/paginated/items?brand=foo&apiKey={apiKey}
&lsPublisherId={Your LinkShare Publisher Id}
 */