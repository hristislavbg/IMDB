package com.imdb.movie.filter;

import com.imdb.actor.entity.Actor;
import lombok.Data;

import java.util.List;

@Data
public class MovieFilter {
  private String name;
  private String genre;
  private String photoUrl;
  private String trailer;
  private Integer year;
  private List<Actor> actorsNames;
  private String columnSort;
  private String sorting;
}
