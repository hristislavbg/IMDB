package com.imdb.movie.controller;

import com.imdb.movie.entity.Movie;
import com.imdb.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(
    value = "/api/v1/movies",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

  private final MovieService movieService;

  @Autowired
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping("")
  public List<Movie> getMovies() throws Exception {
    return movieService.getMovies();
  }

  @GetMapping("/{id}")
  public Movie getMovie(@PathVariable("id") final Long id) throws Exception {
    return movieService.getMovie(id);
  }

  @PostMapping("/create")
  public Movie saveMovie(@RequestBody final Movie movie) throws Exception {
    return movieService.saveMovie(movie);
  }

  @PutMapping("/edit/{id}")
  public Movie editMovie(@PathVariable("id") final Long id, @RequestBody final Movie movie)
      throws Exception {
    return movieService.updateMovie(id, movie);
  }

  @PutMapping("/rating/{id}/{rating}")
  public Movie updateMovieRating(
      @PathVariable("id") Long id, @PathVariable("rating") final Integer rating) throws Exception {
    return movieService.updateMovieRating(id, rating);
  }

  @DeleteMapping("/delete/{id}")
  public List<?> deleteMovie(@PathVariable("id") final Long id) throws Exception {
    movieService.deleteMovie(id);

    return new ArrayList<>();
  }

  @PostMapping("/actor/create/{movieId}/{actorId}")
  public Movie addActorToMovie(
      @PathVariable("movieId") final Long movieId, @PathVariable("actorId") final Long actorId)
      throws Exception {
    return movieService.addActorToMovie(movieId, actorId);
  }

  @PostMapping("/actor/delete/{movieId}/{actorId}")
  public Movie deleteActorFromMovie(
      @PathVariable("movieId") final Long movieId, @PathVariable("actorId") final Long actorId)
      throws Exception {
    return movieService.deleteActorFromMovie(movieId, actorId);
  }

  @GetMapping("/search")
  public List<Movie> getMoviesBySearch(@RequestBody final Map<?, ?> getParams) throws Exception {
    return movieService.searchMovies(getParams);
  }
}
