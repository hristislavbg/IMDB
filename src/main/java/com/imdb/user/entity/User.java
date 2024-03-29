package com.imdb.user.entity;

import com.imdb.movie.entity.Movie;
import com.imdb.role.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data // getters and setters
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(name = "username_unique", columnNames = "username"))
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  private Collection<Role> roles = new ArrayList<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "user_movie",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"))
  private List<Movie> movies;

  public void addMovies(final Movie movie) {
    if (movies == null) {
      movies = new ArrayList<>();
    }

    movies.add(movie);
  }
}
