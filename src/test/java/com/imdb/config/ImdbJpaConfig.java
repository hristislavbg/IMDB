// package com.imdb.config;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.core.env.Environment;
// import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
// import javax.sql.DataSource;
//
// public class ImdbJpaConfig {
//  private Environment environment;
//
//  @Autowired
//  public ImdbJpaConfig(final Environment environment) {
//    this.environment = environment;
//  }
//
//  @Bean
//  public DataSource dataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
//    dataSource.setUrl(environment.getProperty("jdbc.url"));
//    dataSource.setUsername(environment.getProperty("jdbc.user"));
//    dataSource.setPassword(environment.getProperty("jdbc.pass"));
//
//    return dataSource;
//  }
// }
