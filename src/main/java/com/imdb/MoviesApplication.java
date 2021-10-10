package com.imdb;

import com.imdb.role.entity.Role;
import com.imdb.user.entity.User;
import com.imdb.user.repository.UserRepository;
import com.imdb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class MoviesApplication {

  @Autowired UserService userService;

  @Autowired UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(MoviesApplication.class, args);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CommandLineRunner run() {
    // Create initial roles (admin, user) because there will be no roles selection when creating
    // users.
    // By default all newly registered users will be assigned "users" role.
    // Upon application start up, an admin account is created with "admin" role.
    // username: admin, password: admin.

    return args -> {
      User user = userRepository.findByUsername("admin");

      if (user == null) {
        userService.saveRole(new Role(null, "admin"));
        userService.saveRole(new Role(null, "user"));

        userService.saveUser(
            new User(null, "Admin", "admin", "admin", new ArrayList<>(), new ArrayList<>()));

        userService.addRoleToUser("admin", "admin");
      }
    };
  }
}
