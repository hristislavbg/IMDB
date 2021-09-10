package com.imdb.movies.repository;

import com.imdb.movies.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("select a from Actor a where a.name = ?1")
    Actor getActorByName(String name);

    List<Actor> getActorByNameContaining(String name);
}
