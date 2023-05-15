package com.example.imdbcrud.controller;

import com.example.imdbcrud.model.Movie;
import com.example.imdbcrud.repository.MovieRepository;
import com.example.imdbcrud.service.inter.ArtistService;
import com.example.imdbcrud.service.inter.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {
    static final Logger LOGGER = LoggerFactory.getLogger(ArtistController.class);

    MovieService movieService;
    ArtistService artistService;
    private final MovieRepository movieRepository;

    private List<Movie> filteredMovieList = null;

    public MovieController(MovieService movieService, ArtistService artistService,
                           MovieRepository movieRepository) {
        this.movieService = movieService;
        this.artistService = artistService;
        this.movieRepository = movieRepository;
    }

    @GetMapping({"/", "/list", ""})
    public String getMovieList(Model model) {
        return getMoviesByPageNo(model, 1);
    }

    @GetMapping("/page/{no}")
    public String getMoviesByPageNo(Model model, @PathVariable("no") Integer no) {
        Page<Movie> moviesPage = movieService.list(no);

        model.addAttribute("movies", moviesPage.getContent());
        model.addAttribute("currentPage", no);
        model.addAttribute("previousPage", Math.max(no-1, 1));
        model.addAttribute("nextPage",Math.min(no+1,moviesPage.getTotalPages()));
        model.addAttribute("totalPages", moviesPage.getTotalPages());
        model.addAttribute("nbElements", moviesPage.getNumberOfElements());
        model.addAttribute("totalElements", moviesPage.getTotalElements());

        model.addAttribute("rangeA", (no-1)*5+1);
        model.addAttribute("rangeB",Math.min(no*5,moviesPage.getTotalElements()));
        return "movies/list";
    }

    @GetMapping("/sorted")
    public String getSortedList(Model model) {
        return getSortedListPage(model, 1);
    }

    @GetMapping("/sorted/page/{no}")
    public String getSortedListPage(Model model, @PathVariable Integer no) {
        Page<Movie> moviePage = movieService.sortedList(no);

        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", no);
        model.addAttribute("previousPage", Math.max(no-1, 1));
        model.addAttribute("nextPage",Math.min(no+1,moviePage.getTotalPages()));
        model.addAttribute("totalPages", moviePage.getTotalPages());
        model.addAttribute("nbElements", moviePage.getNumberOfElements());
        model.addAttribute("totalElements", moviePage.getTotalElements());

        model.addAttribute("rangeA", (no-1)*5+1);
        model.addAttribute("rangeB",Math.min(no*5,moviePage.getTotalElements()));
        return "movies/sorted";
    }

    @GetMapping("/new")
    public String getNewPage(Model model) {
        model.addAttribute("movie",new Movie());
        return "movies/new";
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("movies/update");
        mv.addObject("movie",movieService.getById(id));
        filteredMovieList = movieRepository.findAll();
        return mv;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        artistService.deleteMovieById(id);
        movieService.deleteById(id);
        filteredMovieList = movieRepository.findAll();
        return "redirect:/movies/";
    }

    @PostMapping("/addNew")
    public String addNew(@ModelAttribute("movie") Movie movie) {
        movieService.save(movie);
        filteredMovieList = movieRepository.findAll();
        return "redirect:/movies";
    }

    @GetMapping("/{id}/cast")
    public String getCastPage(Model model, @PathVariable Long id) {
        model.addAttribute("movie",movieService.getById(id));
        model.addAttribute("cast",movieService.getArtistsById(id));

        return "movies/cast";
    }

    @PostMapping("/updateNew")
    public String updateNew(@ModelAttribute("movie") Movie movie) {
        movieService.save(movie);
        filteredMovieList = movieRepository.findAll();
        return "redirect:/movies";
    }

    @PostMapping("/filter")
    public String filter(Model model, @RequestParam(required = false) String title,
                         @RequestParam(required = false) String description) {
        List<Movie> movies = movieRepository.findAll();

        if(title != null) {
            Collections.sort(movies, Comparator.comparing(Movie::getTitle));
        }

        if(description != null) {
            Collections.sort(movies, Comparator.comparing(Movie::getDescription));
        }


        filteredMovieList = movies;

        model.addAttribute("movies", filteredMovieList);

        return "redirect:/movies/filtered";
    }

    @GetMapping("/filtered")
    public String getFilteredPage(Model model) {
        if(filteredMovieList == null) {
            filteredMovieList = movieRepository.findAll();
        }
        model.addAttribute("movies", filteredMovieList);
        return "/movies/filtered";
    }
}
