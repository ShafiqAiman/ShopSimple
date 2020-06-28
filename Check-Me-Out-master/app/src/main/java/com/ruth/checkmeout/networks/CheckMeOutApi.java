package com.ruth.checkmeout.networks;

import com.ruth.checkmeout.models.CheckMeOutSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CheckMeOutApi {

    @GET("/goods/{id}")
    Call<CheckMeOutSearchResponse> getItem(@Path("id") String code);
}
