package com.imdb.user.service;

import com.imdb.movie.entity.Movie;
import com.imdb.role.entity.Role;
import com.imdb.user.entity.User;

import java.util.List;

public interface UserService {
  User saveUser(User user) throws Exception;

  Role saveRole(Role role);

  User addRoleToUser(String username, String roleName) throws Exception;

  List<User> getUsers() throws Exception;

  User getUser(Long id) throws Exception;

  List<Movie> addFavoriteMovie(Long id) throws Exception;

  List<Movie> deleteFavoriteMovie(Long id) throws Exception;
}
