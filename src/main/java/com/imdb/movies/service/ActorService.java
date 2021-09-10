package com.imdb.movies.service;

import com.imdb.movies.entity.Actor;

import java.util.List;

public interface ActorService {
    Actor saveActor(Actor actor) throws Exception;
    List<Actor> getActors() throws Exception;
}
