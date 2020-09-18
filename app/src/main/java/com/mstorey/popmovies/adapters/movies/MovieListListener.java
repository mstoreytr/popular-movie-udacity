package com.mstorey.popmovies.adapters.movies;

import com.mstorey.popmovies.data.responses.Movie;
// Adapter item click listener
public interface MovieListListener {
    void onMovieClick(Movie movie);
}
