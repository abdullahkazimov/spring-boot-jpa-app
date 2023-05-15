package com.example.imdbcrud.service.inter;

import com.example.imdbcrud.model.Artist;
import com.example.imdbcrud.model.Movie;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieService {
    Page<Movie> list(int pageNo);
    Page<Movie> sortedList(int pageNo);
    Movie getById(Long id);
    Movie save(Movie movie);
    void deleteById(Long id);
    List<Artist> getArtistsById(Long id);
}
