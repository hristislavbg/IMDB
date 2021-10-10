package com.imdb.actor.controller;

import com.imdb.actor.entity.Actor;
import com.imdb.actor.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
    value = "/api/v1/actors",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
public class ActorController {

  private final ActorService actorService;

  @Autowired
  public ActorController(ActorService actorService) {
    this.actorService = actorService;
  }

  @GetMapping("")
  public List<Actor> getActors() throws Exception {
    return actorService.getActors();
  }

  @PostMapping("/create")
  public Actor createActor(@RequestBody final Actor actor) throws Exception {
    return actorService.saveActor(actor);
  }
}
