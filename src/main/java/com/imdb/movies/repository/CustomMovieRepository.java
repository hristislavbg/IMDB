package com.imdb.movies.repository;

import com.imdb.movies.entity.Movie;
import com.imdb.movies.filter.MovieFilter;

import java.util.List;

public interface CustomMovieRepository {
    List<Movie> findAll(MovieFilter movieFilter);
}
