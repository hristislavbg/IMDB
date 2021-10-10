package com.imdb.movie.service.implementation;

import com.imdb.actor.entity.Actor;
import com.imdb.actor.repository.ActorRepository;
import com.imdb.movie.entity.Movie;
import com.imdb.movie.filter.MovieFilter;
import com.imdb.movie.repository.MovieRepository;
import com.imdb.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MovieServiceImplementation implements MovieService {

  private final MovieRepository movieRepository;
  private final ActorRepository actorRepository;

  @Autowired
  public MovieServiceImplementation(
      MovieRepository movieRepository, ActorRepository actorRepository) {
    this.movieRepository = movieRepository;
    this.actorRepository = actorRepository;
  }

  @Override
  public Movie saveMovie(final Movie movie) throws Exception {
    try {
      final Movie checkMovie = movieRepository.getMovieByName(movie.getName());

      if (checkMovie != null) {
        throw new Exception("Movie '" + movie.getName() + "' already exists.");
      }

      movie.setRating(0);
      movie.setVotes(0);
      movie.setRatingSum(0);

      return movieRepository.save(movie);
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public List<Movie> getMovies() throws Exception {
    try {
      return movieRepository.findAll();
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public Movie updateMovie(final Long id, final Movie movie) throws Exception {
    try {
      final List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(id));

      if (checkMovie.isEmpty()) {
        throw new Exception("There are no movies with ID: " + id);
      }

      final Movie movieInstance = movieRepository.getById(id);

      movieInstance.setName(movie.getName());
      movieInstance.setGenre(movie.getGenre());
      movieInstance.setPhotoUrl(movie.getPhotoUrl());
      movieInstance.setTrailer(movie.getTrailer());
      movieInstance.setYear(movie.getYear());

      return movieRepository.save(movieInstance);
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public void deleteMovie(final Long id) throws Exception {
    try {
      final List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(id));

      if (checkMovie.isEmpty()) {
        throw new Exception("There are no movies with ID: " + id);
      }

      final Movie movie = movieRepository.getById(id);

      movie.getActors().clear();
      movieRepository.deleteById(id);

    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public Movie updateMovieRating(final Long id, final Integer rating) throws Exception {
    try {

      if (rating < 1 || rating > 5) {
        throw new Exception("Rating can only be between 1 and 5.");
      }

      List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(id));

      if (checkMovie.isEmpty()) {
        throw new Exception("There are no movies with ID: " + id);
      }

      Movie movie = movieRepository.getById(id);

      final int newVotes = movie.getVotes() + 1;
      final int newRatingSum = movie.getRatingSum() + rating;
      final double newRating = (double) newRatingSum / newVotes;

      final BigDecimal bd = new BigDecimal(newRating).setScale(2, RoundingMode.HALF_UP);
      final double newRatingRounded = bd.doubleValue();

      movie.setRating(newRatingRounded);
      movie.setVotes(newVotes);
      movie.setRatingSum(newRatingSum);

      return movieRepository.save(movie);
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public Movie addActorToMovie(final Long movieId, final Long actorId) throws Exception {
    try {
      final List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(movieId));

      if (checkMovie.isEmpty()) {
        throw new Exception("There are no movies with ID: " + movieId);
      }

      final List<Actor> checkActor = actorRepository.findAllById(Collections.singleton(actorId));

      if (checkActor.isEmpty()) {
        throw new Exception("There are no actors with ID: " + actorId);
      }

      final Movie movie = movieRepository.getById(movieId);
      final Actor actor = actorRepository.getById(actorId);

      final List<Actor> movieActors = movie.getActors();

      if (movieActors.contains(actor)) {
        throw new Exception("Actor already exists with ID: " + actorId);
      }

      movieActors.add(actor);

      return movie;
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public Movie deleteActorFromMovie(final Long movieId, final Long actorId) throws Exception {
    try {
      final List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(movieId));

      if (checkMovie.isEmpty()) {
        throw new Exception("There are no movies with ID: " + movieId);
      }

      final List<Actor> checkActor = actorRepository.findAllById(Collections.singleton(actorId));

      if (checkActor.isEmpty()) {
        throw new Exception("There are no actors with ID: " + actorId);
      }

      final Movie movie = movieRepository.getById(movieId);
      final Actor actor = actorRepository.getById(actorId);

      final List<Actor> movieActors = movie.getActors();

      if (!movieActors.contains(actor)) {
        throw new Exception(
            "Movie with ID: " + movieId + " does not have actor with ID: " + actorId);
      }

      movieActors.remove(actor);

      return movie;
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public Movie getMovie(final Long id) throws Exception {
    try {
      final List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(id));

      if (checkMovie.isEmpty()) {
        throw new Exception("There are no movies with ID: " + id);
      }

      return movieRepository.getById(id);
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public List<Movie> searchMovies(final Map<?, ?> getParams) throws Exception {
    try {
      final MovieFilter movieFilter = new MovieFilter();

      final String name = (String) getParams.get("name");
      final String genre = (String) getParams.get("genre");
      final String photoUrl = (String) getParams.get("photo_url");
      final String trailer = (String) getParams.get("trailer");
      final Integer year = (Integer) getParams.get("year");
      final String actorName = (String) getParams.get("actor_name");
      final String columnSort = (String) getParams.get("column_sort");
      final String sorting = (String) getParams.get("sorting");

      if (name != null) {
        movieFilter.setName(name);
      }

      if (genre != null) {
        movieFilter.setGenre(genre);
      }

      if (photoUrl != null) {
        movieFilter.setPhotoUrl(photoUrl);
      }

      if (trailer != null) {
        movieFilter.setTrailer(trailer);
      }

      if (year != null) {
        movieFilter.setYear(year);
      }

      if (actorName != null) {
        List<Actor> actors = actorRepository.getActorByNameContaining(actorName);

        if (actors != null) {
          movieFilter.setActorsNames(actors);
        }
      }

      if (columnSort != null) {
        movieFilter.setColumnSort(columnSort);
      }

      if (sorting != null && (sorting.equals("asc") || sorting.equals("desc"))) {
        movieFilter.setSorting(sorting);
      }

      return movieRepository.findAll(movieFilter);
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }
}
