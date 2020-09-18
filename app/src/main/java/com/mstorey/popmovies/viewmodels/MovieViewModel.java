package com.mstorey.popmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mstorey.popmovies.data.MovieRespository;
import com.mstorey.popmovies.data.requests.SortByTypes;
import com.mstorey.popmovies.data.responses.Movie;
import com.mstorey.popmovies.data.responses.Review;
import com.mstorey.popmovies.data.responses.Trailer;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    // Repo makes the requests
    private MovieRespository movieRespository;
    // MutableLiveData is used to modify. Observers are attached in View class to get updates to data
    private MutableLiveData<List<Movie>> movieList = new MutableLiveData<>();
    private MutableLiveData<List<Trailer>> trailerList = new MutableLiveData<>();
    private MutableLiveData<List<Review>> reviewList = new MutableLiveData<>();
    private LiveData<List<Movie>> favList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRespository = MovieRespository.getInstance(application);
        // Go ahead and fetch the first page
        fetchMovieList(SortByTypes.MOST_POPULAR.getType(), 1);
        favList = movieRespository.getFavList();
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
    public LiveData<List<Movie>> getFavorites() {
        return favList;
    }

    // getter methods
    public MutableLiveData<List<Movie>> getMovieList() {
        return movieList;
    }
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

    public void fetchMovieList(String type, int page) {
        movieRespository.fetchMoviePage(type, page, movieList);
    }

    public void fetchReviewList(int id, int page) {
        movieRespository.fetchReviews(id, page, reviewList);
    }
}
