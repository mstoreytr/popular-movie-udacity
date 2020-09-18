package com.mstorey.popmovies.data.requests;

import com.mstorey.popmovies.data.responses.MovieListResponse;
import com.mstorey.popmovies.data.responses.ReviewsResponse;
import com.mstorey.popmovies.data.responses.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDBApi {
    //https://api.themoviedb.org/3/movie/<type>?api_key=<key>&page=<page>
    @GET("/3/movie/{type}")
    Call<MovieListResponse> fetchMovieList(@Path("type")String type,
                                           @Query("api_key")String key,
                                           @Query("page")int page);

    //https://api.themoviedb.org/3/movie/{movie_id}/reviews?api_key=<<api_key>>&page=1
    @GET("/3/movie/{movie_id}/reviews")
    Call<ReviewsResponse> fetchReviewList(@Path("movie_id")int movieID,
                                          @Query("api_key")String key,
                                          @Query("page")int page);

    //https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=<<api_key>>
    @GET("/3/movie/{movie_id}/videos")
    Call<TrailersResponse> fetchTrailerList(@Path("movie_id")int movieID,
                                            @Query("api_key")String key);

}
