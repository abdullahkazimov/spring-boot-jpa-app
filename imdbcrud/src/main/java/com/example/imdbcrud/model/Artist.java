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
@Table(name = "ARTISTS")
public class Artist implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String information;

    @ManyToMany
    @JoinTable(
            name = "artist_movie",
            joinColumns = {@JoinColumn(name = "artist_id")},
            inverseJoinColumns = {@JoinColumn(name = "movie_id")}
    )
    private Set<Movie> movies = new HashSet<>();

    public Set<Movie> getMovies() {
        return movies;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(Object o) {
        Artist artist = (Artist) o;

        if(firstName.compareTo(artist.getFirstName()) != 0) {
            return firstName.compareTo(artist.getFirstName());
        }

        if(lastName.compareTo(artist.getLastName()) != 0) {
            return lastName.compareTo(artist.getLastName());
        }

        return information.compareTo(artist.getInformation());
    }
}
