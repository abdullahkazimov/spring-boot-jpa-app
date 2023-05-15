package com.example.imdbcrud.service.impl;

import com.example.imdbcrud.model.Artist;
import com.example.imdbcrud.model.Movie;
import com.example.imdbcrud.repository.ArtistRepository;
import com.example.imdbcrud.service.inter.ArtistService;
import com.example.imdbcrud.service.inter.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ArtistServiceImpl implements ArtistService {

    ArtistRepository artistRepo;
    MovieService movieService;

    public ArtistServiceImpl(ArtistRepository artistRepo, MovieService movieService) {
        this.artistRepo = artistRepo;
        this.movieService = movieService;
    }

    @Override
    public Page<Artist> list(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 5);
        return artistRepo.findAll(pageable);
    }

    @Override
    public Page<Artist> sortedList(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 5,
                Sort.by("firstName").ascending()
                        .and(Sort.by("lastName").ascending()
                                .and(Sort.by("information").ascending())));
        return artistRepo.findAll(pageable);
    }

    @Override
    public Artist getById(Long id) {
        return artistRepo.findById(id).orElse(null);
    }

    @Override
    public Artist save(Artist artist) {
        return artistRepo.save(artist);
    }

    @Override
    public void deleteById(Long id) {
        artistRepo.deleteById(id);
    }

    @Override
    public List<Movie> getMoviesById(Long id) {
        return artistRepo.findMoviesById(id);
    }

    @Override
    public void deleteMovieById(Long id) {
        Movie movie = movieService.getById(id);
        Set<Artist> artists = movie.getArtists();
        for(Artist a : artists) {
            Set<Movie> movies = a.getMovies();
            movies.remove(movie);
            a.setMovies(movies);
        }
    }
}
