package com.mstorey.popmovies.adapters;

import com.mstorey.popmovies.responses.Movie;
// Adapter item click listener
public interface MovieListListener {
    void onMovieClick(Movie movie);
}
