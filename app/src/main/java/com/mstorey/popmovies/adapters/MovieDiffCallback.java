package com.mstorey.popmovies.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.mstorey.popmovies.responses.Movie;

import java.util.Objects;

public class MovieDiffCallback extends DiffUtil.ItemCallback<Movie> {
    // Items are the same if id are the same
    @Override
    public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId());
    }
    @Override
    public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return Objects.equals(oldItem.getPosterPath(), newItem.getPosterPath());
    }
}
