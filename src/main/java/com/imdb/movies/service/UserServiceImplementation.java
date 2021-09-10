package com.imdb.movies.service;

import com.imdb.movies.entity.Movie;
import com.imdb.movies.entity.Role;
import com.imdb.movies.entity.User;
import com.imdb.movies.repository.MovieRepository;
import com.imdb.movies.repository.RoleRepository;
import com.imdb.movies.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MovieRepository movieRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found in the DB");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) throws Exception {
        try {
            User checkUser = userRepository.findByUsername(user.getUsername());

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
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) throws Exception {
        try {
            User user = userRepository.findByUsername(username);
            Role role = roleRepository.findByName(roleName);

            // Adds record to the database without calling the save method because of the Transactions annotation.
            user.getRoles().add(role);
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
    public User getUser(Long id) throws Exception {
        try {
            List<User> checkUser = userRepository.findAllById(Collections.singleton(id));

            if (checkUser.isEmpty()) {
                throw new Exception("There are no users with ID: " + id);
            }

            return userRepository.getById(id);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public List<Movie> addFavoriteMovie(Long id) throws Exception {
        try {
            List<Movie> checkMovies = movieRepository.findAllById(Collections.singleton(id));

            if (checkMovies.isEmpty()) {
                throw new Exception("There are no movies with ID: " + id);
            }

            Movie movie = movieRepository.getById(id);

            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            User user = userRepository.findByUsername(username);

            List<Movie> userMovies = user.getMovies();

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
    public List<Movie> deleteFavoriteMovie(Long id) throws Exception {
        try {
            List<Movie> checkMovies = movieRepository.findAllById(Collections.singleton(id));

            if (checkMovies.isEmpty()) {
                throw new Exception("There are no movies with ID: " + id);
            }

            Movie movie = movieRepository.getById(id);

            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            User user = userRepository.findByUsername(username);

            List<Movie> userMovies = user.getMovies();

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
