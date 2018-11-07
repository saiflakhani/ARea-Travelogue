package com.droidbots.areatravelogue.interfaces;

import com.droidbots.areatravelogue.response.NearbyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearbyInterface {

    @GET("search/2/nearbySearch/.json")
    Call<NearbyResponse> postNearby(@Query("lat") String latitude, @Query("lon") String longitude, @Query("limit") int limit, @Query("idxSet") String idxSet, @Query("key") String API_KEY);

}
