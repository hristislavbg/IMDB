package com.imdb.movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imdb.actor.entity.Actor;
import com.imdb.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
    name = "movies",
    uniqueConstraints = @UniqueConstraint(name = "name_unique", columnNames = "name"))
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "rating")
  @Min(0)
  @Max(5)
  private double rating;

  @Column(name = "rating_sum")
  @JsonIgnore
  private Integer ratingSum;

  @Column(name = "votes")
  private Integer votes;

  @Column(name = "genre", nullable = false)
  private String genre;

  @Column(name = "photo_url")
  private String photoUrl;

  @Column(name = "trailer")
  private String trailer;

  @Column(name = "year", nullable = false)
  private Integer year;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "movie_actor",
      joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
  private List<Actor> actors;

  @JsonIgnore
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "user_movie",
      joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private List<User> users;

  public void addActors(Actor actor) {
    if (actors == null) {
      actors = new ArrayList<>();
    }

    actors.add(actor);
  }

  public void addUsers(User user) {
    if (users == null) {
      users = new ArrayList<>();
    }

    users.add(user);
  }
}
