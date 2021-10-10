package com.imdb.user.serviceImplementation;

import com.imdb.movie.entity.Movie;
import com.imdb.movie.repository.MovieRepository;
import com.imdb.role.entity.Role;
import com.imdb.role.repository.RoleRepository;
import com.imdb.user.entity.User;
import com.imdb.user.repository.UserRepository;
import com.imdb.user.service.implementation.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplementationTest {
  @Mock private UserRepository userRepository;
  @Mock private RoleRepository roleRepository;
  @Mock private MovieRepository movieRepository;
  @Mock private Authentication authentication;
  @Mock private SecurityContext securityContext;

  @InjectMocks UserServiceImplementation userServiceImplementation;

  @Test
  void testLoadUserByUsername() {
    final User user = new User();

    user.setId(1L);
    user.setName("Hristislav");
    user.setUsername("hris");
    user.setPassword("hris");

    when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);

    assertThat(userServiceImplementation.loadUserByUsername(user.getUsername()));

    verify(userRepository, times(1)).findByUsername(Mockito.anyString());
  }

  @Test
  void testSaveUser() {
    final User user = new User();

    user.setId(1L);
    user.setName("Hristislav");
    user.setUsername("hris");
    user.setPassword("hris");

    when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);

    assertThrows(
        Exception.class,
        () -> {
          userServiceImplementation.saveUser(user);
        });

    verify(userRepository, times(1)).findByUsername(Mockito.anyString());
  }

  @Test
  void testAddRole() {
    final Role role = new Role();

    role.setId(1L);
    role.setName("admin");

    when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);

    assertThat(userServiceImplementation.saveRole(role));

    verify(roleRepository, times(1)).save(Mockito.any(Role.class));
  }

  @Test
  void testAddRoleToUser() throws Exception {
    final User user = new User();
    final Role role = new Role();
    final List<Role> roles = new ArrayList<>();

    user.setId(1L);
    user.setName("Hristislav");
    user.setUsername("hris");
    user.setPassword("hris");

    role.setId(1L);
    role.setName("admin");

    roles.add(role);

    when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
    when(roleRepository.findByName(Mockito.anyString())).thenReturn(role);

    assertThat(userServiceImplementation.addRoleToUser(user.getUsername(), role.getName()));

    assertEquals(roles, user.getRoles());

    verify(userRepository, times(1)).findByUsername(Mockito.anyString());
    verify(roleRepository, times(1)).findByName(Mockito.anyString());
  }

  @Test
  void testGetUsers() throws Exception {
    final User user1 = new User();
    final User user2 = new User();
    final List<User> users = new ArrayList<>();

    user1.setId(1L);
    user1.setName("Hristislav");
    user1.setUsername("hris");
    user1.setPassword("hris");

    user2.setId(1L);
    user2.setName("Ivan");
    user2.setUsername("ivan");
    user2.setPassword("ivan");

    users.add(user1);
    users.add(user2);

    when(userRepository.findAll()).thenReturn(users);

    final List<User> findAllList = userServiceImplementation.getUsers();

    verify(userRepository, times(1)).findAll();

    assertEquals(user1, findAllList.get(0));
    assertEquals(user2, users.get(1));
  }

  @Test
  void testGetUserById() throws Exception {
    final User user = new User();
    final List<User> users = new ArrayList<>();

    user.setId(1L);
    user.setName("Hristislav");
    user.setUsername("hris");
    user.setPassword("hris");

    users.add(user);

    when(userRepository.findAllById(Mockito.anyIterable())).thenReturn(users);

    assertThat(userServiceImplementation.getUser(user.getId()));

    verify(userRepository, times(1)).findAllById(Mockito.anyIterable());
  }

  @Test
  void testAddFavoriteMovie() {
    final Movie movie = new Movie();
    final User user = new User();
    final List<Movie> movies = new ArrayList<>();

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);

    user.setId(1L);
    user.setName("Hristislav");
    user.setUsername("hris");
    user.setPassword("hris");

    movies.add(movie);

    when(movieRepository.findAllById(Mockito.anyIterable())).thenReturn(movies);
    when(movieRepository.getById(Mockito.anyLong())).thenReturn(movie);
    when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(user.getUsername());

    SecurityContextHolder.setContext(securityContext);

    assertThrows(
        Exception.class,
        () -> {
          userServiceImplementation.addFavoriteMovie(movie.getId());
        });

    verify(movieRepository, times(1)).findAllById(Mockito.anyIterable());
    verify(movieRepository, times(1)).getById(Mockito.anyLong());
    verify(userRepository, times(1)).findByUsername(Mockito.anyString());
  }

  @Test
  void testDeleteFavoriteMovie() {
    final Movie movie = new Movie();
    final User user = new User();
    final List<Movie> movies = new ArrayList<>();

    movie.setId(1L);
    movie.setName("Titanic");
    movie.setGenre("Drama");
    movie.setYear(2000);

    user.setId(1L);
    user.setName("Hristislav");
    user.setUsername("hris");
    user.setPassword("hris");

    movies.add(movie);

    when(movieRepository.findAllById(Mockito.anyIterable())).thenReturn(movies);
    when(movieRepository.getById(Mockito.anyLong())).thenReturn(movie);
    when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(user.getUsername());

    SecurityContextHolder.setContext(securityContext);

    assertThrows(
        Exception.class,
        () -> {
          userServiceImplementation.deleteFavoriteMovie(movie.getId());
        });

    assertNotEquals(movies, movieRepository.findAll());

    verify(movieRepository, times(1)).findAllById(Mockito.anyIterable());
    verify(movieRepository, times(1)).getById(Mockito.anyLong());
    verify(userRepository, times(1)).findByUsername(Mockito.anyString());
  }
}
