package com.example.imdbcrud.service.impl;

import com.example.imdbcrud.model.Artist;
import com.example.imdbcrud.model.Movie;
import com.example.imdbcrud.repository.MovieRepository;
import com.example.imdbcrud.service.inter.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    MovieRepository movieRepo;

    public MovieServiceImpl(MovieRepository movieRepo) {
        this.movieRepo = movieRepo;
    }

    @Override
    public Page<Movie> list(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 5);
        return movieRepo.findAll(pageable);
    }

    @Override
    public Page<Movie> sortedList(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 5,
                Sort.by("title").ascending()
                        .and(Sort.by("description").ascending()));
        return movieRepo.findAll(pageable);
    }

    @Override
    public Movie getById(Long id) {
        return movieRepo.findById(id).orElse(null);
    }

    @Override
    public Movie save(Movie movie) {
        return movieRepo.save(movie);
    }

    @Override
    public void deleteById(Long id) {
        movieRepo.deleteById(id);
    }

    @Override
    public List<Artist> getArtistsById(Long id) {
        return (List<Artist>) movieRepo.findArtistsById(id);
    }
}
