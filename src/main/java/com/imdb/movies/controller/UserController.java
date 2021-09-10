package com.imdb.movies.controller;

import com.imdb.movies.entity.Movie;
import com.imdb.movies.entity.User;
import com.imdb.movies.repository.UserRepository;
import com.imdb.movies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/v1",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() throws Exception {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PostMapping("/users/register")
    public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception {
        User newUser = userService.saveUser(user);

        userService.addRoleToUser(user.getUsername(), "user");

        return ResponseEntity.ok().body(newUser);
    }

    @GetMapping("/favorite/movies")
    public ResponseEntity<List<Movie>> getFavoriteMovies() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findByUsername(username);

        return ResponseEntity.ok().body(user.getMovies());
    }

    @PostMapping("/favorite/movies/create/{id}")
    public ResponseEntity<List<Movie>> addFavoriteMovie(@PathVariable("id") Long id) throws Exception {
        List<Movie> newMovies = userService.addFavoriteMovie(id);

        return ResponseEntity.ok().body(newMovies);
    }

    @DeleteMapping("/favorite/movies/delete/{id}")
    public ResponseEntity<List<Movie>> deleteFavoriteMovie(@PathVariable("id") Long id) throws Exception {
        List<Movie> newMovies = userService.deleteFavoriteMovie(id);

        return ResponseEntity.ok().body(newMovies);
    }
}
