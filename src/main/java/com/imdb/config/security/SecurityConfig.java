package com.imdb.config.security;

import com.imdb.config.security.filter.CustomAuthenticationFilter;
import com.imdb.config.security.filter.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public SecurityConfig(
      UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CustomAuthenticationFilter customAuthenticationFilter =
        new CustomAuthenticationFilter(authenticationManagerBean());
    // Rewrite the default path for login in order to match the api convention.
    customAuthenticationFilter.setFilterProcessesUrl("/api/v1/users/login");

    http.csrf().disable();

    http.sessionManagement().sessionCreationPolicy(STATELESS);

    http.authorizeRequests().antMatchers("/api/v1/users/login").permitAll();
    http.authorizeRequests().antMatchers("/api/v1/users/register").permitAll();

    http.authorizeRequests().antMatchers(GET, "/api/v1/users").hasAnyAuthority("admin");
    http.authorizeRequests().antMatchers(GET, "/api/v1/users/{id}").hasAnyAuthority("admin");
    http.authorizeRequests().antMatchers(GET, "/api/v1/users/favorite/movies").authenticated();
    http.authorizeRequests()
        .antMatchers(POST, "/api/v1/users/favorite/movies/create/{id}")
        .authenticated();
    http.authorizeRequests()
        .antMatchers(DELETE, "/api/v1/users/favorite/movies/delete/{id}")
        .authenticated();

    http.authorizeRequests().antMatchers(GET, "/api/v1/movies").permitAll();
    http.authorizeRequests().antMatchers(GET, "/api/v1/movies/{id}").permitAll();
    http.authorizeRequests().antMatchers(POST, "/api/v1/movies/create").hasAuthority("admin");
    http.authorizeRequests().antMatchers(PUT, "/api/v1/movies/edit/{id}").hasAuthority("admin");
    http.authorizeRequests()
        .antMatchers(DELETE, "/api/v1/movies/delete/{id}")
        .hasAuthority("admin");
    http.authorizeRequests()
        .antMatchers(PUT, "/api/v1/movies/rating/{id}/{rating}")
        .authenticated();
    http.authorizeRequests()
        .antMatchers(POST, "/api/v1/movies/actor/create/{movieId}/{actorId}")
        .hasAuthority("admin");
    http.authorizeRequests()
        .antMatchers(POST, "/api/v1/movies/actor/delete/{movieId}/{actorId}")
        .hasAuthority("admin");
    http.authorizeRequests().antMatchers(GET, "/api/v1/movies/search").permitAll();

    http.authorizeRequests().antMatchers(GET, "/api/v1/actors").permitAll();
    http.authorizeRequests().antMatchers(POST, "/api/v1/actors/create").hasAuthority("admin");

    http.authorizeRequests().anyRequest().authenticated();

    http.addFilter(customAuthenticationFilter);
    http.addFilterBefore(
        new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
