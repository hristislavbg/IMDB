package com.imdb.config.logger;

import com.imdb.logger.interceptor.LoggerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoggerConfig implements WebMvcConfigurer {
  private final LoggerInterceptor loggerInterceptor;

  @Autowired
  public LoggerConfig(LoggerInterceptor loggerInterceptor) {
    this.loggerInterceptor = loggerInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loggerInterceptor);
  }
}
