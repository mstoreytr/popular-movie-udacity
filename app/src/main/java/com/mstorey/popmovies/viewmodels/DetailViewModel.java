package com.mstorey.popmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mstorey.popmovies.data.MovieRespository;
import com.mstorey.popmovies.data.responses.Movie;
import com.mstorey.popmovies.data.responses.Review;
import com.mstorey.popmovies.data.responses.Trailer;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {
    // Repo makes the requests
    private MovieRespository movieRespository;
    // MutableLiveData is used to modify. Observers are attached in View class to get updates to data
    private MutableLiveData<List<Trailer>> trailerList = new MutableLiveData<>();
    private MutableLiveData<List<Review>> reviewList = new MutableLiveData<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
        movieRespository = MovieRespository.getInstance(application);
    }

    // Room methods
    public void addFav(Movie movie) {
        movieRespository.addFavorite(movie);
    }
    public void removeFav(Movie movie) {
        movieRespository.removeFavorite(movie);
    }

    public LiveData<Movie> getFavorite(Movie movie) {
        return movieRespository.getFavorite(movie.getId());
    }
    // getter methods
    public MutableLiveData<List<Trailer>> getTrailerList() {
        return trailerList;
    }
    public MutableLiveData<List<Review>> getReviewList() {
        return reviewList;
    }

    // Fetch methods
    public void fetchTrailer(int id) {
        movieRespository.fetchTrailers(id, trailerList);
    }

    public void fetchReviewList(int id, int page) {
        movieRespository.fetchReviews(id, page, reviewList);
    }
}
