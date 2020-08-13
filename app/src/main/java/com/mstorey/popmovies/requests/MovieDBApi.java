package com.mstorey.popmovies.requests;

import com.mstorey.popmovies.responses.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDBApi {
    //https://api.themoviedb.org/3/movie/<type>?api_key=<key>&page=<page>
    @GET("/3/movie/{type}")
    Call<MovieListResponse> fetchMovieList(@Path("type")String id, @Query("api_key")String key, @Query("page")int page);

}
