package com.imdb.actor.service.implementation;

import com.imdb.actor.entity.Actor;
import com.imdb.actor.repository.ActorRepository;
import com.imdb.actor.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ActorServiceImplementation implements ActorService {

  private final ActorRepository actorRepository;

  @Autowired
  public ActorServiceImplementation(ActorRepository actorRepository) {
    this.actorRepository = actorRepository;
  }

  @Override
  public Actor saveActor(final Actor actor) throws Exception {
    try {
      final Actor checkActor = actorRepository.getActorByName(actor.getName());

      if (checkActor != null) {
        throw new Exception("Actor '" + actor.getName() + "' already exists.");
      }

      return actorRepository.save(actor);
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public List<Actor> getActors() throws Exception {
    try {
      return actorRepository.findAll();
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }
}
