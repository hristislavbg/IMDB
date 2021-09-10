package com.imdb.movies.repository;

import com.imdb.movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, CustomMovieRepository {
    @Query("select m from Movie m where m.name = ?1")
    Movie getMovieByName(String name);
}
