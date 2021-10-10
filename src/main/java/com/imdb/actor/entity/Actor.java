package com.imdb.actor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imdb.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
    name = "actors",
    uniqueConstraints = @UniqueConstraint(name = "name_unique", columnNames = "name"))
public class Actor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
      name = "movie_actor",
      joinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"))
  private List<Movie> movies;

  public void addMovies(Movie movie) {
    if (movies == null) {
      movies = new ArrayList<>();
    }

    movies.add(movie);
  }
}
