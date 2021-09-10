package com.imdb.movies.repository;

import com.imdb.movies.entity.Actor;
import com.imdb.movies.entity.Movie;
import com.imdb.movies.filter.MovieFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class MovieRepositoryImpl implements CustomMovieRepository{

    @Autowired
    EntityManager entityManager;

    @Override
    public List<Movie> findAll(MovieFilter movieFilter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> rootEntity = query.from(Movie.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(movieFilter.getName())) {
            Path<String> name = rootEntity.get("name");
            predicates.add(
                    criteriaBuilder.like(
                            name,
                            "%" + movieFilter.getName() + "%"
                    )
            );

        }

        if (StringUtils.hasText(movieFilter.getGenre())) {
            Path<String> name = rootEntity.get("genre");
            predicates.add(
                    criteriaBuilder.like(
                            name,
                            "%" + movieFilter.getGenre() + "%"
                    )
            );
        }

        if (StringUtils.hasText(movieFilter.getPhotoUrl())) {
            Path<String> name = rootEntity.get("photo_url");
            predicates.add(
                    criteriaBuilder.like(
                            name,
                            "%" + movieFilter.getPhotoUrl() + "%"
                    )
            );
        }

        if (StringUtils.hasText(movieFilter.getTrailer())) {
            Path<String> name = rootEntity.get("trailer");
            predicates.add(
                    criteriaBuilder.like(
                            name,
                            "%" + movieFilter.getTrailer() + "%"
                    )
            );
        }

        if (movieFilter.getYear() != null) {
            Path<Integer> year = rootEntity.get("year");
            predicates.add(
                    criteriaBuilder.equal(
                            year,
                            movieFilter.getYear()
                    )
            );
        }

        if (movieFilter.getActorsNames() != null) {
            Join<Movie, Actor> actors = rootEntity.join("actors", JoinType.LEFT);
            predicates.add(actors.in(movieFilter.getActorsNames()));
        }

        if (movieFilter.getColumnSort() != null) {

            String columnSort = movieFilter.getColumnSort();
            String sorting = movieFilter.getSorting();

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

        query.where(predicates.toArray(new Predicate[]{}));
        query.distinct(true);

        return entityManager.createQuery(query).getResultList();
    }
}
