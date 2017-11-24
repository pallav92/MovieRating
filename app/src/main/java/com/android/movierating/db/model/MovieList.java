package com.android.movierating.db.model;

import java.util.List;

/**
 * Created by pallav on 14/11/17.
 */

public class MovieList {


    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
