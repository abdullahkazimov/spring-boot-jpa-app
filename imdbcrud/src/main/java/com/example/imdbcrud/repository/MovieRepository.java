package com.example.imdbcrud.repository;

import com.example.imdbcrud.model.Artist;
import com.example.imdbcrud.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select a from Artist a where a in " +
            "(select a from Artist a left join a.movies movie where movie.id = :id)")
    List<Artist> findArtistsById(Long id);

    @Override
    Page<Movie> findAll(Pageable pageable);
}
