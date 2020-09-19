package com.mstorey.popmovies.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mstorey.popmovies.R;
import com.mstorey.popmovies.data.database.FavoritesDao;
import com.mstorey.popmovies.data.database.FavoritesDatabase;
import com.mstorey.popmovies.data.requests.MovieDBApi;
import com.mstorey.popmovies.data.responses.Movie;
import com.mstorey.popmovies.data.responses.MovieListResponse;
import com.mstorey.popmovies.data.responses.Review;
import com.mstorey.popmovies.data.responses.ReviewsResponse;
import com.mstorey.popmovies.data.responses.Trailer;
import com.mstorey.popmovies.data.responses.TrailersResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    Class for managing network requests and Room operations
 */
public class MovieRespository {
    // TODO ADD MOVIEDB KEY HERE
    private String movieKEY = "";
    private MovieDBApi movieDBApi;
    private FavoritesDao favoritesDao;
    private static MovieRespository movieRespository;
    private LiveData<List<Movie>> favList;
    private ExecutorService threadHandler = Executors.newCachedThreadPool();
    public static MovieRespository getInstance(Context context) {
        if (movieRespository == null) {
            movieRespository = new MovieRespository(context);
        }
        return movieRespository;
    }

    public  MovieRespository(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.movie_db_url))
                .addConverterFactory(GsonConverterFactory.create()).build();
        movieDBApi = retrofit.create(MovieDBApi.class);
        FavoritesDatabase favDB = FavoritesDatabase.getInstance(context);
        favoritesDao = favDB.favoritesDao();
        favList = favoritesDao.getFavorites();
    }

    public LiveData<List<Movie>> getFavList() {
        return favList;
    }

    public LiveData<Movie> getFavorite(int movieID) {
        return favoritesDao.getFavorite(movieID);
    }

    public void addFavorite(Movie movie) {
        threadHandler.execute(() -> favoritesDao.insertFavorite(movie));
    }
    public void removeFavorite(Movie movie) {
        threadHandler.execute(() -> favoritesDao.deleteFavorite(movie));
    }

    public void fetchMoviePage(String type, int page, MutableLiveData<List<Movie>> movieList, MutableLiveData<Boolean> isLoading){
        isLoading.postValue(true);
        movieDBApi.fetchMovieList(type, movieKEY, page).enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieListResponse> call, @NonNull Response<MovieListResponse> response) {
                isLoading.postValue(false);
                if (response.body() != null) {
                    List<Movie> fullList = new ArrayList<>();
                    if (movieList.getValue() != null) {
                        fullList = movieList.getValue();
                    }
                    fullList.addAll(response.body().getMovies());
                    movieList.postValue(fullList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieListResponse> call, @NonNull Throwable t) {
                isLoading.postValue(false);
                Log.e(this.getClass().getName(), t.toString());
            }
        });
    }

    public void fetchTrailers(int id, MutableLiveData<List<Trailer>> trailerList){
        movieDBApi.fetchTrailerList(id, movieKEY).enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailersResponse> call, @NonNull Response<TrailersResponse> response) {
                if (response.body() != null) {
                    trailerList.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailersResponse> call, @NonNull Throwable t) {
                Log.e(this.getClass().getName(), t.toString());
            }
        });
    }

    public void fetchReviews(int id, int page, MutableLiveData<List<Review>> reviewList){
        movieDBApi.fetchReviewList(id, movieKEY, page).enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsResponse> call, @NonNull Response<ReviewsResponse> response) {
                if (response.body() != null) {
                    reviewList.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsResponse> call, @NonNull Throwable t) {
                Log.e(this.getClass().getName(), t.toString());
            }
        });
    }
}
