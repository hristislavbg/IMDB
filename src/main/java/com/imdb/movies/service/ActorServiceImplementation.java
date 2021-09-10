package com.imdb.movies.service;

import com.imdb.movies.entity.Actor;
import com.imdb.movies.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ActorServiceImplementation implements ActorService {

    @Autowired
    ActorRepository actorRepository;

    @Override
    public Actor saveActor(Actor actor) throws Exception {
        try {
            Actor checkActor = actorRepository.getActorByName(actor.getName());

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
