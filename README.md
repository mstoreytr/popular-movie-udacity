# popular-movie-udacity
Popular Movie App for Udacity Project Stage 2

This android app uses the Movie DB api along with Retrofit to fetch popular and top rated movies from these endpoints.

- https://developers.themoviedb.org/3/movies/get-popular-movies

- https://developers.themoviedb.org/3/movies/get-top-rated-movies

Stage 2 includes trailers and reviews fetched from the following endpoints.

- https://developers.themoviedb.org/3/movies/get-movie-videos

- https://developers.themoviedb.org/3/movies/get-movie-reviews

The results are parsed to objects via GSON

The results are then displayed in a Recyclerview that uses a listadapter

The dropdown in the top right corner can switch the view from "Most Popular" to "Highest Rated"

Once a user clicks a item, the movie object is sent in an intent to a new screen to display detailed info like plot or rating.

To handle network connection related errors, the ACCESS_NETWORK_STATE permission and ConnectivityManager are used to retry requests on network restoration

## Using MovieDB API
Please follow the steps for creating an account listed here

https://developers.themoviedb.org/3/getting-started/introduction

Once you have a key of your own please update the field "movieKEY" in MovieListActivity.java or use the TODO finder in Android Studio to quickly find it.
