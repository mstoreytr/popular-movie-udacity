package com.mstorey.popmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mstorey.popmovies.data.MovieRespository;
import com.mstorey.popmovies.data.requests.SortByTypes;
import com.mstorey.popmovies.data.responses.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    // Repo makes the requests
    private MovieRespository movieRespository;
    // MutableLiveData is used to modify. Observers are attached in View class to get updates to data
    private MutableLiveData<List<Movie>> fullMovieList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private LiveData<List<Movie>> favList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRespository = MovieRespository.getInstance(application);
        // Go ahead and fetch the first page
        fetchMovieList(SortByTypes.MOST_POPULAR.getType(), 1);
        favList = movieRespository.getFavList();
    }

    public boolean isLoading() {
        if (isLoading == null) {
            return false;
        }
        return isLoading.getValue();
    }
    // getter methods
    public MutableLiveData<List<Movie>> getMovieList() {
        return fullMovieList;
    }
    public LiveData<List<Movie>> getFavorites() {
        return favList;
    }
    // Fetch methods
    public void fetchMovieList(String type, int page) {
        movieRespository.fetchMoviePage(type, page, fullMovieList, isLoading);
    }
}
