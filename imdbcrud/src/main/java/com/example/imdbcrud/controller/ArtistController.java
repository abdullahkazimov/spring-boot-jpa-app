package com.example.imdbcrud.controller;

import com.example.imdbcrud.model.Artist;
import com.example.imdbcrud.repository.ArtistRepository;
import com.example.imdbcrud.service.inter.ArtistService;
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
@RequestMapping("/artists")
public class ArtistController {
    static final Logger LOGGER = LoggerFactory.getLogger(ArtistController.class);
    ArtistService artistService;
    private final ArtistRepository artistRepository;

    public ArtistController(ArtistService artistService,
                            ArtistRepository artistRepository) {
        this.artistService = artistService;
        this.artistRepository = artistRepository;
    }

    @GetMapping({"/", "/list", ""})
    public String getArtistList(Model model) {
        return getArtistsByPageNo(model, 1);
    }

    @GetMapping("/page/{no}")
    public String getArtistsByPageNo(Model model, @PathVariable("no") Integer no) {
        Page<Artist> artistsPage = artistService.list(no);

        model.addAttribute("artists", artistsPage.getContent());
        model.addAttribute("currentPage", no);
        model.addAttribute("previousPage", Math.max(no-1, 1));
        model.addAttribute("nextPage",Math.min(no+1,artistsPage.getTotalPages()));
        model.addAttribute("totalPages", artistsPage.getTotalPages());
        model.addAttribute("nbElements", artistsPage.getNumberOfElements());
        model.addAttribute("totalElements", artistsPage.getTotalElements());

        model.addAttribute("rangeA", (no-1)*5+1);
        model.addAttribute("rangeB",Math.min(no*5,artistsPage.getTotalElements()));
        return "artists/list";
    }

    @GetMapping("/sorted")
    public String getSortedList(Model model) {
        return getSortedListPage(model, 1);
    }

    @GetMapping("/sorted/page/{no}")
    public String getSortedListPage(Model model, @PathVariable Integer no) {
        Page<Artist> artistsPage = artistService.sortedList(no);

        model.addAttribute("artists", artistsPage.getContent());
        model.addAttribute("currentPage", no);
        model.addAttribute("previousPage", Math.max(no-1, 1));
        model.addAttribute("nextPage",Math.min(no+1,artistsPage.getTotalPages()));
        model.addAttribute("totalPages", artistsPage.getTotalPages());
        model.addAttribute("nbElements", artistsPage.getNumberOfElements());
        model.addAttribute("totalElements", artistsPage.getTotalElements());

        model.addAttribute("rangeA", (no-1)*5+1);
        model.addAttribute("rangeB",Math.min(no*5,artistsPage.getTotalElements()));
        return "artists/sorted";
    }

    @GetMapping("/new")
    public String getNewPage(Model model) {
        model.addAttribute("artist",new Artist());
        return "artists/new";
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdatePage(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("artists/update");
        mv.addObject("artist",artistService.getById(id));
        filteredArtistList = artistRepository.findAll();
        return mv;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        artistService.deleteById(id);
        filteredArtistList = artistRepository.findAll();
        return "redirect:/artists/";
    }

    @PostMapping("/addNew")
    public String addNew(@ModelAttribute("artist") Artist artist) {
        artistService.save(artist);
        filteredArtistList = artistRepository.findAll();
        return "redirect:/artists";
    }

    @GetMapping("/{id}/filmography")
    public String getFilmographyPage(Model model, @PathVariable Long id) {
        model.addAttribute("artist",artistService.getById(id));
        model.addAttribute("filmography",artistService.getMoviesById(id));
        return "artists/filmography";
    }

    @PostMapping("/updateNew")
    public String updateNew(@ModelAttribute("artist") Artist artist) {
        artistService.save(artist);
        filteredArtistList = artistRepository.findAll();
        return "redirect:/artists";
    }

    private List<Artist> filteredArtistList = null;

    @PostMapping("/filter")
    public String filter(Model model, @RequestParam(required = false) String firstName,
                         @RequestParam(required = false) String lastName,
                         @RequestParam(required = false) String information) {
        List<Artist> artists = artistRepository.findAll();
        if(firstName != null) {

            Collections.sort(artists, Comparator.comparing(Artist::getFirstName));
        }

        if(lastName != null) {

            Collections.sort(artists, Comparator.comparing(Artist::getLastName));
        }

        if(information != null) {

            Collections.sort(artists, Comparator.comparing(Artist::getInformation));
        }



        filteredArtistList = artists;

        model.addAttribute("artists", filteredArtistList);

        return "redirect:/artists/filtered";
    }

    @GetMapping("/filtered")
    public String getFilteredPage(Model model) {
        if(filteredArtistList == null) {
            filteredArtistList = artistRepository.findAll();
        }
        model.addAttribute("artists", filteredArtistList);
        return "/artists/filtered";
    }
}
