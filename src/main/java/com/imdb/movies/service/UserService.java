package com.imdb.movies.service;

import com.imdb.movies.entity.Movie;
import com.imdb.movies.entity.Role;
import com.imdb.movies.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user) throws Exception;
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName) throws Exception;
    List<User> getUsers() throws Exception;
    User getUser(Long id) throws Exception;
    List<Movie> addFavoriteMovie(Long id) throws Exception;
    List<Movie> deleteFavoriteMovie(Long id) throws Exception;
}
