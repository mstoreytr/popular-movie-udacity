package com.mstorey.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mstorey.popmovies.adapters.MovieDiffCallback;
import com.mstorey.popmovies.adapters.MovieListAdapter;
import com.mstorey.popmovies.adapters.MovieListListener;
import com.mstorey.popmovies.requests.MovieDBApi;
import com.mstorey.popmovies.requests.SortByTypes;
import com.mstorey.popmovies.responses.Movie;
import com.mstorey.popmovies.responses.MovieListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListActivity extends AppCompatActivity implements MovieListListener {

    // TODO() PLACE YOUR MOVIE-DB API KEY HERE
    private String movieKEY = "";
    private int currentPage = 1;
    private SortByTypes currentType = SortByTypes.MOST_POPULAR;
    private Boolean isLoading = false;
    private Boolean connectionLost = false;
    private MovieDBApi api;
    private MovieListAdapter adapter;
    private Callback<MovieListResponse> responseCallback;
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
        setUpMovieApi();
        // Finally fetch the first page
        fetchMovies(currentPage);
    }

    private void setUpBaseView() {
        setContentView(R.layout.activity_main);
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
                    fetchMovies(currentPage);
                }
            }
        });
        connectionLost = cm.getActiveNetworkInfo() == null;
    }

    private void setUpMovieApi() {
        // Set up requests
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.movie_db_url)).addConverterFactory(GsonConverterFactory.create()).build();
        this.api = retrofit.create(MovieDBApi.class);
        // set up response handling
        responseCallback = new Callback<MovieListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieListResponse> call, @NonNull Response<MovieListResponse> response) {
                isLoading = false;
                // Listadapter's list can't be modified so create a copy and append new items
                List<Movie> currentList = new ArrayList<>(adapter.getCurrentList());
                if (response.body() != null) {
                    currentList.addAll(response.body().getMovies());
                    adapter.submitList(currentList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieListResponse> call, @NonNull Throwable t) {
                isLoading = false;
                Log.e(this.getClass().getName(), t.toString());
            }
        };
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
                        fetchMovies(currentPage);
                    }
                }
            }
        });
    }

    private void fetchMovies(int page) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        api.fetchMovieList(currentType.getType(), movieKEY, page).enqueue(responseCallback);
    }

    // Option menu for the Sort by feature
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            resetAndFetchForType(SortByTypes.MOST_POPULAR);
            return true;
        }
        if (id == R.id.action_rated) {
            resetAndFetchForType(SortByTypes.HIGHEST_RATED);
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
            fetchMovies(currentPage);
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