package com.mstorey.popmovies.responses;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@Keep
public class MovieListResponse {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("results")
    @Expose
    private List<Movie> movies = null;

    public int getPage() { return page; }
    public void setPage(int value) { this.page = value; }

    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int value) { this.totalResults = value; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int value) { this.totalPages = value; }

    public List<Movie> getMovies() { return movies; }
    public void setMovies(List<Movie> value) { this.movies = value; }
}
