package com.mstorey.popmovies.requests;

public enum SortByTypes {
    HIGHEST_RATED("top_rated"),
    MOST_POPULAR("popular");
    String type;
    SortByTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
