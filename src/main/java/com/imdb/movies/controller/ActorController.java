package com.imdb.movies.controller;

import com.imdb.movies.entity.Actor;
import com.imdb.movies.repository.ActorRepository;
import com.imdb.movies.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/v1",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class ActorController {

    @Autowired
    ActorService actorService;

    @Autowired
    ActorRepository actorRepository;

    @GetMapping("/actors")
    public ResponseEntity<List<Actor>> getActors() throws Exception {
        return ResponseEntity.ok().body(actorService.getActors());
    }

    @PostMapping("/actors/create")
    public ResponseEntity<?> createActor(@RequestBody Actor actor) throws Exception {
        Actor newActor = actorService.saveActor(actor);

        return ResponseEntity.ok().body(newActor);
    }
}
