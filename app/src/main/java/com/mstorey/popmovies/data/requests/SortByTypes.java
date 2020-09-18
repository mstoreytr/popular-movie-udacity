package com.mstorey.popmovies.data.requests;

public enum SortByTypes {
    HIGHEST_RATED("top_rated"),
    MOST_POPULAR("popular"),
    FAVORITE("favorites");
    String type;
    SortByTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
