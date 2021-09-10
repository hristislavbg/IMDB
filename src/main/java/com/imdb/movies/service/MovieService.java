package com.imdb.movies.service;

import com.imdb.movies.entity.Movie;

import java.util.List;
import java.util.Map;

public interface MovieService {
    Movie saveMovie(Movie movie) throws Exception;
    List<Movie> getMovies() throws Exception;
    void updateMovie(Long id, Movie movie) throws Exception;
    void deleteMovie(Long id) throws Exception;
    void updateMovieRating(Long id, Integer rating) throws Exception;
    void addActorToMovie(Long movieId, Long actorId) throws Exception;
    void deleteActorFromMovie(Long movieId, Long actorId) throws Exception;
    Movie getMovie(Long id) throws Exception;
    List<Movie> searchMovies(Map<?, ?> getParams) throws Exception;
}
