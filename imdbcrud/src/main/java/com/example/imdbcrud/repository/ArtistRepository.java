package com.example.imdbcrud.repository;

import com.example.imdbcrud.model.Artist;
import com.example.imdbcrud.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @Query("select m from Movie m where m in " +
            "(select m from Movie m left join m.artists artist where artist.id = :id)")
    List<Movie> findMoviesById(Long id);

    @Override
    Page<Artist> findAll(Pageable pageable);
}
