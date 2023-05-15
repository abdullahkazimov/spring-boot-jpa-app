package com.example.imdbcrud.service.inter;

import com.example.imdbcrud.model.Artist;
import com.example.imdbcrud.model.Movie;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArtistService {
    Page<Artist> list(int pageNo);
    Page<Artist> sortedList(int pageNo);
    Artist getById(Long id);
    Artist save(Artist artist);
    void deleteById(Long id);
    List<Movie> getMoviesById(Long id);
    void deleteMovieById(Long id);
}
