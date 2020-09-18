package com.mstorey.popmovies.adapters.reviews;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.mstorey.popmovies.data.responses.Review;

import java.util.Objects;

public class ReviewDiffCallback extends DiffUtil.ItemCallback<Review> {
    @Override
    public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId());
    }
    @Override
    public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
        return Objects.equals(oldItem.getContent(), newItem.getContent());
    }
}
