package com.imdb.user.service.implementation;

import com.imdb.movie.entity.Movie;
import com.imdb.movie.repository.MovieRepository;
import com.imdb.role.entity.Role;
import com.imdb.role.repository.RoleRepository;
import com.imdb.user.entity.User;
import com.imdb.user.repository.UserRepository;
import com.imdb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserServiceImplementation implements UserService, UserDetailsService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final MovieRepository movieRepository;

  @Autowired
  public UserServiceImplementation(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      MovieRepository movieRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.movieRepository = movieRepository;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User not found in the DB");
    }

    final Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getRoles()
        .forEach(
            role -> {
              authorities.add(new SimpleGrantedAuthority(role.getName()));
            });

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), authorities);
  }

  @Override
  public User saveUser(final User user) throws Exception {
    try {
      final User checkUser = userRepository.findByUsername(user.getUsername());

      if (checkUser != null) {
        throw new Exception("User '" + user.getUsername() + "' already exists.");
      }

      user.setPassword(passwordEncoder.encode(user.getPassword()));
      return userRepository.save(user);
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public Role saveRole(final Role role) {
    return roleRepository.save(role);
  }

  @Override
  public User addRoleToUser(final String username, final String roleName) throws Exception {
    try {
      final User user = userRepository.findByUsername(username);
      final Role role = roleRepository.findByName(roleName);

      // Adds record to the database without calling the save method because of the Transactions
      // annotation.
      user.getRoles().add(role);

      return user;
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public List<User> getUsers() throws Exception {
    try {
      return userRepository.findAll();
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public User getUser(final Long id) throws Exception {
    try {
      final List<User> checkUser = userRepository.findAllById(Collections.singleton(id));

      if (checkUser.isEmpty()) {
        throw new Exception("There are no users with ID: " + id);
      }

      return userRepository.getById(id);
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public List<Movie> addFavoriteMovie(final Long id) throws Exception {
    try {
      final List<Movie> checkMovies = movieRepository.findAllById(Collections.singleton(id));

      if (checkMovies.isEmpty()) {
        throw new Exception("There are no movies with ID: " + id);
      }

      final Movie movie = movieRepository.getById(id);

      final String username =
          (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      final User user = userRepository.findByUsername(username);

      final List<Movie> userMovies = user.getMovies();

      if (userMovies.contains(movie)) {
        throw new Exception("Movie '" + movie.getName() + "' has already been set as favorite.");
      }

      userMovies.add(movie);

      return userMovies;
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }

  @Override
  public List<Movie> deleteFavoriteMovie(final Long id) throws Exception {
    try {
      final List<Movie> checkMovies = movieRepository.findAllById(Collections.singleton(id));

      if (checkMovies.isEmpty()) {
        throw new Exception("There are no movies with ID: " + id);
      }

      final Movie movie = movieRepository.getById(id);

      final String username =
          (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      final User user = userRepository.findByUsername(username);

      final List<Movie> userMovies = user.getMovies();

      if (!userMovies.contains(movie)) {
        throw new Exception("Movie '" + movie.getName() + "' is not part of your favorites list.");
      }

      userMovies.remove(movie);

      return userMovies;
    } catch (Exception exception) {
      throw new Exception(exception.getMessage());
    }
  }
}
