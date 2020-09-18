package com.mstorey.popmovies;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mstorey.popmovies.adapters.reviews.ReviewDiffCallback;
import com.mstorey.popmovies.adapters.reviews.ReviewListAdapter;
import com.mstorey.popmovies.adapters.trailers.TrailerDiffCallback;
import com.mstorey.popmovies.adapters.trailers.TrailerListAdapter;
import com.mstorey.popmovies.adapters.trailers.TrailerListListener;
import com.mstorey.popmovies.data.responses.Movie;
import com.mstorey.popmovies.data.responses.Trailer;
import com.mstorey.popmovies.viewmodels.MovieViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity implements TrailerListListener {

    private Movie detailedMovie;
    private MovieViewModel movieViewModel;

    private LinearLayout reviewContainer;
    private LinearLayout trailerContainer;
    private RecyclerView reviewRecyclerView;
    private RecyclerView trailerRecyclerView;
    private Button favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        // First get the movie from the intent
        detailedMovie = (Movie) Objects.requireNonNull(getIntent().getExtras()).get(getString(R.string.movie_intent));
        if (detailedMovie == null) {
            // No movie to show so finish
            finish();
        }
        setUpToolbar();
        setUpDetailView();
        setUpTrailersAndReviews();
        setUpFavorite();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setTitle(detailedMovie.getTitle());
    }

    private void setUpDetailView() {
        TextView title = findViewById(R.id.movie_title);
        TextView year = findViewById(R.id.movie_year);
        TextView rating = findViewById(R.id.movie_rating);
        TextView plot = findViewById(R.id.plot_label);
        ImageView poster = findViewById(R.id.movie_poster);
        reviewRecyclerView = findViewById(R.id.review_list);
        reviewContainer = findViewById(R.id.review_container);
        trailerContainer = findViewById(R.id.trailer_container);
        trailerRecyclerView = findViewById(R.id.trailer_list);
        favButton = findViewById(R.id.fav_button);

        title.setText(detailedMovie.getTitle());
        year.setText(formatReleaseDate());
        rating.setText(getString(R.string.rating_string, detailedMovie.getVoteAverage()));
        plot.setText(detailedMovie.getOverview());
        Glide.with(this).load(getString(R.string.poster_url, detailedMovie.getPosterPath())).into(poster);
    }

    private void setUpTrailersAndReviews() {
        reviewRecyclerView.setAdapter(new ReviewListAdapter(new ReviewDiffCallback()));
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trailerRecyclerView.setAdapter(new TrailerListAdapter(new TrailerDiffCallback(), this));
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getReviewList().observe(this, reviews -> {
            reviewContainer.setVisibility(reviews.isEmpty() ? View.GONE : View.VISIBLE);
            if (reviewRecyclerView.getAdapter() instanceof ReviewListAdapter) {
                ReviewListAdapter adapter = (ReviewListAdapter) reviewRecyclerView.getAdapter();
                adapter.submitList(reviews);
            }
        });
        movieViewModel.getTrailerList().observe(this, trailers -> {
            trailerContainer.setVisibility(trailers.isEmpty() ? View.GONE : View.VISIBLE);
            if (trailerRecyclerView.getAdapter() instanceof TrailerListAdapter) {
                TrailerListAdapter adapter = (TrailerListAdapter) trailerRecyclerView.getAdapter();
                adapter.submitList(trailers);
            }
        });
        // Fetch reviews and trailers, their observers will update the ui
        movieViewModel.fetchReviewList(detailedMovie.getId(), 1);
        movieViewModel.fetchTrailer(detailedMovie.getId());
    }

    private void setUpFavorite() {
        // The button selected state is based on the observer not on the click listener
        movieViewModel.getFavorite(detailedMovie).observe(this, movie -> {
            boolean added = movie != null;
            favButton.setText(added ? getString(R.string.favorite_text_remove) : getString(R.string.favorite_text_add));
            favButton.setSelected(added);
        });
        // Calling the Room updates methods will trigger the observer to update the selected state
        favButton.setOnClickListener(view -> {
            if (view.isSelected()) {
                movieViewModel.removeFav(detailedMovie);
            } else {
                movieViewModel.addFav(detailedMovie);
            }
        });
    }

    // Make release date look readable
    private String formatReleaseDate() {
        String date = detailedMovie.getReleaseDate();
        try {
            // year comes back as yyyy-MM-dd
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.US);
            Date relDate = simpleDateFormat.parse(date);
            if (relDate != null) {
                date = displayDateFormat.format(relDate);
            }
        } catch (ParseException e) {
            // Found a weird date during formatting, so just raw value is used
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onTrailerClick(Trailer movie) {
        // All of the trailers I found in api requests were youtube, so open an Intent using youtube url
        Uri trailerURL = Uri.parse(getString(R.string.trailer_url_youtube, movie.getKey()));
        Intent intent = new Intent(Intent.ACTION_VIEW, trailerURL);
        // make sure we don't crash on a bad intent
        if (getPackageManager() != null && getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.bad_intent_message, Toast.LENGTH_LONG).show();
        }
    }
}