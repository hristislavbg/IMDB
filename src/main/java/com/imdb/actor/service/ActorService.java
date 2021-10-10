package com.imdb.actor.service;

import com.imdb.actor.entity.Actor;

import java.util.List;

public interface ActorService {
  Actor saveActor(Actor actor) throws Exception;

  List<Actor> getActors() throws Exception;
}
