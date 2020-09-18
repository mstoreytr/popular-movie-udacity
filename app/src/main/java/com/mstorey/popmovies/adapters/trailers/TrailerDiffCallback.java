package com.mstorey.popmovies.adapters.trailers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.mstorey.popmovies.data.responses.Review;
import com.mstorey.popmovies.data.responses.Trailer;

import java.util.Objects;

public class TrailerDiffCallback extends DiffUtil.ItemCallback<Trailer> {
    @Override
    public boolean areItemsTheSame(@NonNull Trailer oldItem, @NonNull Trailer newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId());
    }
    @Override
    public boolean areContentsTheSame(@NonNull Trailer oldItem, @NonNull Trailer newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName());
    }
}
