package com.mstorey.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mstorey.popmovies.adapters.movies.MovieDiffCallback;
import com.mstorey.popmovies.adapters.movies.MovieListAdapter;
import com.mstorey.popmovies.adapters.movies.MovieListListener;
import com.mstorey.popmovies.data.requests.SortByTypes;
import com.mstorey.popmovies.data.responses.Movie;
import com.mstorey.popmovies.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity implements MovieListListener {

    // TODO() PLACE YOUR MOVIE-DB API KEY HERE
    private String movieKEY = "";
    private int currentPage = 1;
    private SortByTypes currentType = SortByTypes.MOST_POPULAR;
    private Boolean isLoading = false;
    private Boolean connectionLost = false;
    private MovieViewModel movieViewModel;
    private MovieListAdapter adapter;
    private RecyclerView movieRecyclerView;
    // For restoring list
    private Parcelable movieRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (movieRecyclerViewState != null) {
            // Already have a saved state, must be restoring from background
            return;
        }
        checkNetworkConnection();
        setUpBaseView();
        setUpRecyclerView();
        setUpViewModel();
    }

    private void setUpViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovieList().observe(this, movies -> {
            // Listadapter's list can't be modified so create a copy and append new items
            List<Movie> currentList = new ArrayList<>(adapter.getCurrentList());
            if (movies != null) {
                currentList.addAll(movies);
                adapter.submitList(currentList);
            }
        });
        // We have to start observing for the live data to receive updates
        movieViewModel.getFavorites().observe(this, movies -> {});
    }

    private void setUpBaseView() {
        setContentView(R.layout.main_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // hold list state
        if (movieRecyclerView.getLayoutManager() != null) {
            movieRecyclerViewState = movieRecyclerView.getLayoutManager().onSaveInstanceState();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (movieRecyclerViewState != null && movieRecyclerView.getLayoutManager() != null) {
            movieRecyclerView.getLayoutManager().onRestoreInstanceState(movieRecyclerViewState);
        }
    }

    private void checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.registerNetworkCallback(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                connectionLost = true;
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if (connectionLost) {
                    connectionLost = false;
                    movieViewModel.fetchMovieList(currentType.getType(), currentPage);
                }
            }
        });
        connectionLost = cm.getActiveNetworkInfo() == null;
    }

    private void setUpRecyclerView() {
        movieRecyclerView = findViewById(R.id.movie_list);
        // Make it a grid layout
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // Create a list adapter
        this.adapter = new MovieListAdapter(new MovieDiffCallback(), this);
        movieRecyclerView.setAdapter(adapter);

        // Add scroll listener for paging
        movieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter.getItemCount() > 1 && !isLoading) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null
                            && layoutManager.findLastVisibleItemPosition()+ 15 > adapter.getItemCount()) {
                        currentPage++;
                        movieViewModel.fetchMovieList(currentType.getType(), currentPage);
                    }
                }
            }
        });
    }
    // Option menu for the Sort by feature
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        switch (item.getItemId()) {
            case R.id.action_popular:
                resetAndFetchForType(SortByTypes.MOST_POPULAR);
                return true;
            case R.id.action_rated:
                resetAndFetchForType(SortByTypes.HIGHEST_RATED);
                return true;
            case R.id.action_favorite:
                resetAndFetchForType(SortByTypes.FAVORITE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Takes a new Sort by type and refreshes the movie list
     * @param newType New type to sort by
     */
    private void resetAndFetchForType(SortByTypes newType) {
        if (currentType != newType) {
            currentType = newType;
            currentPage = 1;
            adapter.submitList(null);
            if (newType == SortByTypes.FAVORITE) {
                adapter.submitList(movieViewModel.getFavorites().getValue());
            } else {
                movieViewModel.fetchMovieList(currentType.getType(), currentPage);
            }
        }
    }

    @Override
    // Click callback for opening item selection
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getString(R.string.movie_intent), movie);
        startActivity(intent);
    }
}