package com.imdb.logger.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component
public class LoggerInterceptor implements HandlerInterceptor {
  private final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss")
          .withLocale(Locale.UK)
          .withZone(ZoneId.systemDefault());

  private final Instant instant = Instant.now();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    log.info(
        "\nAttempt to make a request: { \n Request URL: {}"
            + "\n Time: {} \n Requested Method: {} \n}",
        request.getRequestURI(),
        dateTimeFormatter.format(Instant.now()),
        request.getMethod());
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
      throws Exception {
    log.info(
        "\nRequest is executed: { \n Request URL: {}"
            + "\n Time: {} \n Requested Method: {} \n Exception: {} \n}",
        request.getRequestURI(),
        dateTimeFormatter.format(Instant.now()),
        request.getMethod(),
        exception != null ? exception.getMessage() : "");
  }
}
