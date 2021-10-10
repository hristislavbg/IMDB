package com.imdb.actor.serviceImplementation;

import com.imdb.actor.entity.Actor;
import com.imdb.actor.repository.ActorRepository;
import com.imdb.actor.service.implementation.ActorServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActorServiceImplementationTest {
  @Mock private ActorRepository actorRepository;

  @InjectMocks ActorServiceImplementation actorServiceImplementation;

  @Test
  void addActor() {
    final Actor actor = new Actor();

    actor.setId(1L);
    actor.setName("Brad Pitt");

    when(actorRepository.getActorByName(Mockito.anyString())).thenReturn(actor);

    Assertions.assertThrows(
        Exception.class,
        () -> {
          actorServiceImplementation.saveActor(actor);
        });

    verify(actorRepository, times(1)).getActorByName(Mockito.anyString());
  }

  @Test
  void testGetActors() throws Exception {
    final Actor actor1 = new Actor();
    final Actor actor2 = new Actor();
    final List<Actor> actors = new ArrayList<>();

    actor1.setId(1L);
    actor1.setName("Brad Pitt");

    actor2.setId(1L);
    actor2.setName("Dwayne Johnson");

    actors.add(actor1);
    actors.add(actor2);

    when(actorRepository.findAll()).thenReturn(actors);

    assertThat(actorServiceImplementation.getActors());
  }
}
