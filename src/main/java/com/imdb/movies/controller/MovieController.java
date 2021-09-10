package com.imdb.movies.controller;

import com.imdb.movies.entity.Movie;
import com.imdb.movies.repository.MovieRepository;
import com.imdb.movies.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(
        value = "/api/v1",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies() throws Exception {
        return ResponseEntity.ok().body(movieService.getMovies());
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok().body(movieService.getMovie(id));
    }

    @PostMapping("/movies/create")
    public ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) throws Exception {
        Movie newMovie = movieService.saveMovie(movie);

        return ResponseEntity.ok().body(newMovie);
    }

    @PutMapping("/movies/edit/{id}")
    public ResponseEntity<Movie> editMovie(@PathVariable("id") Long id, @RequestBody Movie movie) throws Exception {
        movieService.updateMovie(id, movie);

        return ResponseEntity.ok().body(movieRepository.getById(id));
    }

    @PutMapping("/movies/rating/{id}/{rating}")
    public ResponseEntity<Movie> updateMovieRating(
            @PathVariable("id") Long id,
            @PathVariable("rating") Integer rating
    ) throws Exception {
        movieService.updateMovieRating(id, rating);

        return ResponseEntity.ok().body(movieRepository.getById(id));
    }

    @DeleteMapping("/movies/delete/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable("id") Long id) throws Exception {
        movieService.deleteMovie(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/movies/actor/create/{movieId}/{actorId}")
    public ResponseEntity<Movie> addActorToMovie(
            @PathVariable("movieId") Long movieId,
            @PathVariable("actorId") Long actorId
    ) throws Exception {
        movieService.addActorToMovie(movieId, actorId);

        return ResponseEntity.ok().body(movieRepository.getById(movieId));
    }

    @PostMapping("/movies/actor/delete/{movieId}/{actorId}")
    public ResponseEntity<Movie> deleteActorFromMovie(
            @PathVariable("movieId") Long movieId,
            @PathVariable("actorId") Long actorId
    ) throws Exception {
        movieService.deleteActorFromMovie(movieId, actorId);

        return ResponseEntity.ok().body(movieRepository.getById(movieId));
    }

    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> getMoviesBySearch(@RequestBody Map<?, ?> getParams) throws Exception {
        List<Movie> movies = movieService.searchMovies(getParams);

        return ResponseEntity.ok().body(movies);
    }
}
