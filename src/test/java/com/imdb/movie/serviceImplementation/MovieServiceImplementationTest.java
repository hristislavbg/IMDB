package com.imdb.movie.serviceImplementation;

import com.imdb.actor.entity.Actor;
import com.imdb.actor.repository.ActorRepository;
import com.imdb.movie.entity.Movie;
import com.imdb.movie.repository.MovieRepository;
import com.imdb.movie.service.implementation.MovieServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplementationTest {
  @Mock MovieRepository movieRepository;

  @Mock ActorRepository actorRepository;

  @InjectMocks MovieServiceImplementation movieServiceImplementation;

  @Test
  void testSaveMovie() {
    final Movie movie = new Movie();

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);

    when(movieRepository.getMovieByName(anyString())).thenReturn(movie);

    assertThrows(
        Exception.class,
        () -> {
          movieServiceImplementation.saveMovie(movie);
        });

    verify(movieRepository, times(1)).getMovieByName(anyString());
  }

  @Test
  void testGetMovies() {
    final Movie movie1 = new Movie();
    final Movie movie2 = new Movie();
    final List<Movie> movies = new ArrayList<>();

    movie1.setId(1L);
    movie1.setName("Titanic");
    movie1.setGenre("Drama");
    movie1.setYear(2000);

    movie2.setId(1L);
    movie2.setName("Titanic");
    movie2.setGenre("Drama");
    movie2.setYear(2000);

    movies.add(movie1);
    movies.add(movie2);

    when(movieRepository.findAllById(anyIterable())).thenReturn(movies);

    assertThrows(
        Exception.class,
        () -> {
          movieServiceImplementation.getMovies();
        });

    verify(movieRepository, times(1)).findAllById(anyIterable());
  }

  @Test
  void testUpdateMovie() throws Exception {
    final Movie movie = new Movie();
    final Movie movieUpdate = new Movie();
    final List<Movie> movies = new ArrayList<>();

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);

    movieUpdate.setId(1L);
    movieUpdate.setName("Titanic 1");
    movieUpdate.setGenre("Drama");
    movieUpdate.setYear(2000);

    movies.add(movie);

    when(movieRepository.findAllById(anyIterable())).thenReturn(movies);
    when(movieRepository.getById(anyLong())).thenReturn(movie);

    assertThat(movieServiceImplementation.updateMovie(movie.getId(), movieUpdate));

    assertEquals("Titanic 1", movie.getName());

    verify(movieRepository, times(1)).findAllById(anyIterable());
    verify(movieRepository, times(1)).getById(anyLong());
  }

  @Test
  void testDeleteMovie() {
    final Movie movie = new Movie();
    final List<Movie> movies = new ArrayList<>();

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);

    movies.add(movie);

    when(movieRepository.findAllById(anyIterable())).thenReturn(movies);
    when(movieRepository.getById(anyLong())).thenReturn(movie);

    assertThrows(
        Exception.class,
        () -> {
          movieServiceImplementation.deleteMovie(movie.getId());
        });

    verify(movieRepository, times(1)).findAllById(anyIterable());
    verify(movieRepository, times(1)).getById(anyLong());
  }

  @Test
  void testUpdateMovieRating() throws Exception {
    final Movie movie = new Movie();
    final List<Movie> movies = new ArrayList<>();

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);

    movies.add(movie);

    when(movieRepository.findAllById(anyIterable())).thenReturn(movies);
    when(movieRepository.getById(anyLong())).thenReturn(movie);

    assertThat(movieServiceImplementation.saveMovie(movie));
    assertThat(movieServiceImplementation.updateMovieRating(movie.getId(), 4));

    assertEquals(4, movie.getRating());

    verify(movieRepository, times(1)).findAllById(anyIterable());
    verify(movieRepository, times(1)).getById(anyLong());
  }

  @Test
  void testAddActorToMovie() throws Exception {
    final Movie movie = new Movie();
    final Actor actor = new Actor();
    final List<Movie> movies = new ArrayList<>();
    final List<Actor> initialMovieActors = new ArrayList<>();
    final List<Actor> movieActors = new ArrayList<>();

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);
    movie.setActors(initialMovieActors);

    movies.add(movie);

    actor.setId(1L);
    actor.setName("Brad Pitt");

    movieActors.add(actor);

    when(movieRepository.findAllById(anyIterable())).thenReturn(movies);
    when(actorRepository.findAllById(anyIterable())).thenReturn(movieActors);

    when(movieRepository.getById(anyLong())).thenReturn(movie);
    when(actorRepository.getById(anyLong())).thenReturn(actor);

    assertThat(movieServiceImplementation.addActorToMovie(movie.getId(), actor.getId()));

    assertEquals(movieActors, movie.getActors());

    verify(movieRepository, times(1)).findAllById(anyIterable());
    verify(actorRepository, times(1)).findAllById(anyIterable());

    verify(movieRepository, times(1)).getById(anyLong());
    verify(actorRepository, times(1)).getById(anyLong());
  }

  @Test
  void testDeleteActorFromMovie() throws Exception {
    final Movie movie = new Movie();
    final Actor actor = new Actor();
    final List<Movie> movies = new ArrayList<>();
    final List<Actor> movieActors = new ArrayList<>();

    actor.setId(1L);
    actor.setName("Brad Pitt");

    movieActors.add(actor);

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);
    movie.setActors(movieActors);

    movies.add(movie);

    when(movieRepository.findAllById(anyIterable())).thenReturn(movies);
    when(actorRepository.findAllById(anyIterable())).thenReturn(movieActors);

    when(movieRepository.getById(anyLong())).thenReturn(movie);
    when(actorRepository.getById(anyLong())).thenReturn(actor);

    assertThat(movieServiceImplementation.deleteActorFromMovie(movie.getId(), actor.getId()));

    assertEquals(new ArrayList<>(), movie.getActors());

    verify(movieRepository, times(1)).findAllById(anyIterable());
    verify(actorRepository, times(1)).findAllById(anyIterable());

    verify(movieRepository, times(1)).getById(anyLong());
    verify(actorRepository, times(1)).getById(anyLong());
  }

  @Test
  void testGetMovie() throws Exception {
    final Movie movie = new Movie();
    final List<Movie> movies = new ArrayList<>();

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);

    movies.add(movie);

    when(movieRepository.findAllById(anyIterable())).thenReturn(movies);

    assertThat(movieServiceImplementation.getMovie(movie.getId()));

    verify(movieRepository, times(1)).findAllById(anyIterable());
  }

  @Test
  void testSearchMovies() throws Exception {
    final Map<String, String> searchParams = new HashMap<>();

    searchParams.put("actor_name", "Brad Pitt");

    when(actorRepository.getActorByNameContaining(anyString())).thenReturn(new ArrayList<>());

    assertThat(movieServiceImplementation.searchMovies(searchParams));

    verify(actorRepository, times(1)).getActorByNameContaining(anyString());
  }
}
