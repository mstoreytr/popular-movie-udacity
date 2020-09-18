package com.mstorey.popmovies.adapters.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mstorey.popmovies.R;
import com.mstorey.popmovies.data.responses.Review;

public class ReviewListAdapter extends ListAdapter<Review, ReviewListAdapter.ReviewViewHolder> {

    public ReviewListAdapter(@NonNull DiffUtil.ItemCallback<Review> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = getItem(position);
        if (review != null) {
            holder.author.setText(review.getAuthor());
            holder.review.setText(review.getContent());
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView review;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author_textview);
            review = itemView.findViewById(R.id.review_textview);
        }
    }
}
