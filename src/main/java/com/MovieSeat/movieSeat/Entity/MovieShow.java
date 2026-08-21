package com.MovieSeat.movieSeat.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieShow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String showId= UUID.randomUUID().toString();
    private LocalDate showStartDate;


    @ManyToOne
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private LocalTime time;


    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;


    @ManyToMany(mappedBy = "shows")
    private List<User> users=new ArrayList<>();

    @OneToMany(mappedBy = "movieShow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShowSeat> showSeats = new ArrayList<>();

    @OneToMany(mappedBy = "movieShow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
}
