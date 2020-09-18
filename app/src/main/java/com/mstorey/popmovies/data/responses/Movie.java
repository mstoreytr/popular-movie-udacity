package com.mstorey.popmovies.data.responses;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Using gson and room annotations since both models are the same
 */
@Keep
@Entity(tableName = "favorites")
public class Movie implements Serializable {

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    @Expose
    private double popularity;

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    @ColumnInfo(name = "video")
    @SerializedName("video")
    @Expose
    private boolean video;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    @Expose
    private boolean adult;

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    private String overview;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    public double getPopularity() { return popularity; }
    public void setPopularity(double value) { this.popularity = value; }

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int value) { this.voteCount = value; }

    public boolean getVideo() { return video; }
    public void setVideo(boolean value) { this.video = value; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String value) { this.posterPath = value; }

    public int getId() { return id; }
    public void setId(int value) { this.id = value; }

    public boolean getAdult() { return adult; }
    public void setAdult(boolean value) { this.adult = value; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String value) { this.backdropPath = value; }

    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String value) { this.originalLanguage = value; }

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String value) { this.originalTitle = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(double value) { this.voteAverage = value; }

    public String getOverview() { return overview; }
    public void setOverview(String value) { this.overview = value; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String value) { this.releaseDate = value; }
}
