package com.mstorey.popmovies.adapters.trailers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mstorey.popmovies.R;
import com.mstorey.popmovies.data.responses.Trailer;

public class TrailerListAdapter extends ListAdapter<Trailer, TrailerListAdapter.TrailerViewHolder> {
    private TrailerListListener listListener;
    public TrailerListAdapter(@NonNull DiffUtil.ItemCallback<Trailer> diffCallback, TrailerListListener trailerListListener) {
        super(diffCallback);
        this.listListener = trailerListListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = getItem(position);
        if (trailer != null) {
            holder.trailer.setText(trailer.getName());
            holder.trailer.setOnClickListener(view -> {
                listListener.onTrailerClick(trailer);
            });
        }
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        Button trailer;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            trailer = itemView.findViewById(R.id.trailer_button);
        }
    }
}
