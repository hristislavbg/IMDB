package com.imdb.movie.service;

import com.imdb.movie.entity.Movie;

import java.util.List;
import java.util.Map;

public interface MovieService {
  Movie saveMovie(Movie movie) throws Exception;

  List<Movie> getMovies() throws Exception;

  Movie updateMovie(Long id, Movie movie) throws Exception;

  void deleteMovie(Long id) throws Exception;

  Movie updateMovieRating(Long id, Integer rating) throws Exception;

  Movie addActorToMovie(Long movieId, Long actorId) throws Exception;

  Movie deleteActorFromMovie(Long movieId, Long actorId) throws Exception;

  Movie getMovie(Long id) throws Exception;

  List<Movie> searchMovies(Map<?, ?> getParams) throws Exception;
}
