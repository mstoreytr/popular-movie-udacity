package com.mstorey.popmovies.data.requests;

public enum SortByTypes {
    HIGHEST_RATED("top_rated"),
    MOST_POPULAR("popular"),
    FAVORITE("favorites");
    String type;
    SortByTypes(String type) {
        this.type = type;
    }

    public static SortByTypes getFromString(String input) {
        for (SortByTypes type : SortByTypes.values()) {
            if (type.getType().equalsIgnoreCase(input)) {
                return type;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }
}
