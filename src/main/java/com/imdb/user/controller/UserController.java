package com.imdb.user.controller;

import com.imdb.movie.entity.Movie;
import com.imdb.user.entity.User;
import com.imdb.user.repository.UserRepository;
import com.imdb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
    value = "/api/v1/users",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  private final UserService userService;
  private final UserRepository userRepository;

  @Autowired
  public UserController(UserService userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }

  @GetMapping("")
  public List<User> getUsers() throws Exception {
    return userService.getUsers();
  }

  @GetMapping("/{id}")
  public User getUser(@PathVariable("id") final Long id) throws Exception {
    return userService.getUser(id);
  }

  @PostMapping("/register")
  public User saveUser(@RequestBody User user) throws Exception {
    final User newUser = userService.saveUser(user);

    return userService.addRoleToUser(user.getUsername(), "user");
  }

  @GetMapping("/favorite/movies")
  public List<Movie> getFavoriteMovies() {
    final String username =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    final User user = userRepository.findByUsername(username);

    return user.getMovies();
  }

  @PostMapping("/favorite/movies/create/{id}")
  public List<Movie> addFavoriteMovie(@PathVariable("id") final Long id) throws Exception {
    return userService.addFavoriteMovie(id);
  }

  @DeleteMapping("/favorite/movies/delete/{id}")
  public List<Movie> deleteFavoriteMovie(@PathVariable("id") final Long id) throws Exception {
    return userService.deleteFavoriteMovie(id);
  }
}
