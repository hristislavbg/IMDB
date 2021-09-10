package com.imdb.movies.service;

import com.imdb.movies.entity.Actor;
import com.imdb.movies.entity.Movie;
import com.imdb.movies.filter.MovieFilter;
import com.imdb.movies.repository.ActorRepository;
import com.imdb.movies.repository.MovieRepository;
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

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ActorRepository actorRepository;

    @Override
    public Movie saveMovie(Movie movie) throws Exception {
        try {
            Movie checkMovie = movieRepository.getMovieByName(movie.getName());

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
    public void updateMovie(Long id, Movie movie) throws Exception {
        try {
            List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(id));

            if (checkMovie.isEmpty()) {
                throw new Exception("There are no movies with ID: " + id);
            }

            Movie movieInstance = movieRepository.getById(id);

            movieInstance.setName(movie.getName());
            movieInstance.setGenre(movie.getGenre());
            movieInstance.setPhotoUrl(movie.getPhotoUrl());
            movieInstance.setTrailer(movie.getTrailer());
            movieInstance.setYear(movie.getYear());

            movieRepository.save(movieInstance);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public void deleteMovie(Long id) throws Exception {
        try{
            List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(id));

            if (checkMovie.isEmpty()) {
                throw new Exception("There are no movies with ID: " + id);
            }

            Movie movie = movieRepository.getById(id);

            movie.getActors().clear();
            movieRepository.deleteById(id);

        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }

    }

    @Override
    public void updateMovieRating(Long id, Integer rating) throws Exception {
        try {

            if (rating < 1 || rating > 5) {
                throw new Exception("Rating can only be between 1 and 5.");
            }

            List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(id));

            if (checkMovie.isEmpty()) {
                throw new Exception("There are no movies with ID: " + id);
            }

            Movie movie = movieRepository.getById(id);

            int newVotes = movie.getVotes() + 1;
            int newRatingSum = movie.getRatingSum() + rating;
            double newRating = (double) newRatingSum / newVotes;

            BigDecimal bd = new BigDecimal(newRating).setScale(2, RoundingMode.HALF_UP);
            double newRatingRounded = bd.doubleValue();

            movie.setRating(newRatingRounded);
            movie.setVotes(newVotes);
            movie.setRatingSum(newRatingSum);

            movieRepository.save(movie);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public void addActorToMovie(Long movieId, Long actorId) throws Exception {
        try {
            List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(movieId));

            if (checkMovie.isEmpty()) {
                throw new Exception("There are no movies with ID: " + movieId);
            }

            List<Actor> checkActor = actorRepository.findAllById(Collections.singleton(actorId));

            if (checkActor.isEmpty()) {
                throw new Exception("There are no actors with ID: " + actorId);
            }

            Movie movie = movieRepository.getById(movieId);
            Actor actor = actorRepository.getById(actorId);

            List<Actor> movieActors = movie.getActors();

            if (movieActors.contains(actor)) {
                throw new Exception("Actor already exists with ID: " + actorId);
            }

            movieActors.add(actor);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public void deleteActorFromMovie(Long movieId, Long actorId) throws Exception {
        try {
            List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(movieId));

            if (checkMovie.isEmpty()) {
                throw new Exception("There are no movies with ID: " + movieId);
            }

            List<Actor> checkActor = actorRepository.findAllById(Collections.singleton(actorId));

            if (checkActor.isEmpty()) {
                throw new Exception("There are no actors with ID: " + actorId);
            }

            Movie movie = movieRepository.getById(movieId);
            Actor actor = actorRepository.getById(actorId);

            List<Actor> movieActors = movie.getActors();

            if (!movieActors.contains(actor)) {
                throw new Exception("Movie with ID: " + movieId + " does not have actor with ID: " + actorId);
            }

            movieActors.remove(actor);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public Movie getMovie(Long id) throws Exception {
        try {
            List<Movie> checkMovie = movieRepository.findAllById(Collections.singleton(id));

            if (checkMovie.isEmpty()) {
                throw new Exception("There are no movies with ID: " + id);
            }

            return movieRepository.getById(id);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public List<Movie> searchMovies(Map<?, ?> getParams) throws Exception {
        try {
            MovieFilter movieFilter = new MovieFilter();

            String name = (String) getParams.get("name");
            String genre = (String) getParams.get("genre");
            String photoUrl = (String) getParams.get("photo_url");
            String trailer = (String) getParams.get("trailer");
            Integer year = (Integer) getParams.get("year");
            String actorName = (String) getParams.get("actor_name");
            String columnSort = (String) getParams.get("column_sort");
            String sorting = (String) getParams.get("sorting");

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
