package com.imdb.movie.repository;

import com.imdb.movie.entity.Movie;
import com.imdb.movie.filter.MovieFilter;

import java.util.List;

public interface CustomMovieRepository {
  List<Movie> findAll(MovieFilter movieFilter);
}
