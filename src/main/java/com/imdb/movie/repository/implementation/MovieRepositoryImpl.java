package com.imdb.movie.repository.implementation;

import com.imdb.actor.entity.Actor;
import com.imdb.movie.entity.Movie;
import com.imdb.movie.filter.MovieFilter;
import com.imdb.movie.repository.CustomMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class MovieRepositoryImpl implements CustomMovieRepository {

  private final EntityManager entityManager;

  @Autowired
  public MovieRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<Movie> findAll(final MovieFilter movieFilter) {
    final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Movie> query = criteriaBuilder.createQuery(Movie.class);
    final Root<Movie> rootEntity = query.from(Movie.class);

    final List<Predicate> predicates = new ArrayList<>();

    if (StringUtils.hasText(movieFilter.getName())) {
      final Path<String> name = rootEntity.get("name");
      predicates.add(criteriaBuilder.like(name, "%" + movieFilter.getName() + "%"));
    }

    if (StringUtils.hasText(movieFilter.getGenre())) {
      final Path<String> name = rootEntity.get("genre");
      predicates.add(criteriaBuilder.like(name, "%" + movieFilter.getGenre() + "%"));
    }

    if (StringUtils.hasText(movieFilter.getPhotoUrl())) {
      final Path<String> name = rootEntity.get("photo_url");
      predicates.add(criteriaBuilder.like(name, "%" + movieFilter.getPhotoUrl() + "%"));
    }

    if (StringUtils.hasText(movieFilter.getTrailer())) {
      final Path<String> name = rootEntity.get("trailer");
      predicates.add(criteriaBuilder.like(name, "%" + movieFilter.getTrailer() + "%"));
    }

    if (movieFilter.getYear() != null) {
      final Path<Integer> year = rootEntity.get("year");
      predicates.add(criteriaBuilder.equal(year, movieFilter.getYear()));
    }

    if (movieFilter.getActorsNames() != null) {
      final Join<Movie, Actor> actors = rootEntity.join("actors", JoinType.LEFT);
      predicates.add(actors.in(movieFilter.getActorsNames()));
    }

    if (movieFilter.getColumnSort() != null) {

      final String columnSort = movieFilter.getColumnSort();
      final String sorting = movieFilter.getSorting();

      if (sorting != null && sorting.equals("asc")) {
        query.orderBy(criteriaBuilder.asc(rootEntity.get(columnSort)));
      } else if (sorting != null && sorting.equals("desc")) {
        query.orderBy(criteriaBuilder.desc(rootEntity.get(columnSort)));
      } else {
        query.orderBy(criteriaBuilder.asc(rootEntity.get(columnSort)));
      }
    } else {
      query.orderBy(criteriaBuilder.asc(rootEntity.get("name")));
    }

    query.where(predicates.toArray(new Predicate[] {}));
    query.distinct(true);

    return entityManager.createQuery(query).getResultList();
  }
}
