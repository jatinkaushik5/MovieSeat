package com.MovieSeat.movieSeat.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @OneToMany(mappedBy = "city")
    private List<MovieShow> movieShows;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Theatre> theatres;
}
