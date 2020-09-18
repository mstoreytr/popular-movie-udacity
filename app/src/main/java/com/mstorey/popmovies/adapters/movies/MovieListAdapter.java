package com.mstorey.popmovies.adapters.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mstorey.popmovies.R;
import com.mstorey.popmovies.data.responses.Movie;

public class MovieListAdapter extends ListAdapter<Movie, MovieListAdapter.MovieViewHolder> {
    private static final String MOVIEDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w342";
    private MovieListListener listListener;
    public MovieListAdapter(@NonNull DiffUtil.ItemCallback<Movie> diffCallback, MovieListListener movieListListener) {
        super(diffCallback);
        this.listListener = movieListListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        Context currentContext = holder.poster.getContext();
        if (movie != null && currentContext != null) {
            Glide.with(currentContext).load(currentContext.getString(R.string.poster_url, movie.getPosterPath())).into(holder.poster);
            holder.poster.setOnClickListener(view -> {
                listListener.onMovieClick(movie);
            });
            holder.title.setText(movie.getTitle());
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_movie_poster);
            title = itemView.findViewById(R.id.item_movie_title);
        }
    }
}
