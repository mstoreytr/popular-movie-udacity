package com.mstorey.popmovies;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mstorey.popmovies.responses.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    private Movie detailedMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        // First get the movie from the intent
        detailedMovie = (Movie) Objects.requireNonNull(getIntent().getExtras()).get("MOVIE");
        if (detailedMovie == null) {
            // No movie to show so finish
            finish();
        }
        toolBarLayout.setTitle(detailedMovie.getTitle());
        TextView year = findViewById(R.id.movie_year);
        TextView rating = findViewById(R.id.movie_rating);
        TextView plot = findViewById(R.id.plot_label);
        ImageView poster = findViewById(R.id.movie_poster);

        year.setText(getString(R.string.year_string, formatReleaseDate()));
        rating.setText(getString(R.string.rating_string, detailedMovie.getVoteAverage()));
        plot.setText(getString(R.string.plot_string, detailedMovie.getOverview()));
        Picasso.get().load("https://image.tmdb.org/t/p/w342"+detailedMovie.getPosterPath()).into(poster);

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
            // Found a weird date, so just raw value
            e.printStackTrace();
        }
        return date;
    }
}