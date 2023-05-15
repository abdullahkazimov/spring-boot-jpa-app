package com.example.imdbcrud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MOVIES")
public class Movie implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @ManyToMany(mappedBy = "movies")
    private Set<Artist> artists = new HashSet<>();

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    @Override
    public int compareTo(Object o) {
        Movie movie = (Movie) o;
        if(title.compareTo(movie.getTitle()) != 0) {
            return title.compareTo(movie.getTitle());
        }

        return description.compareTo(movie.getDescription());
    }
}
