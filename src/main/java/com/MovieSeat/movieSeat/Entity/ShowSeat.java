package com.MovieSeat.movieSeat.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private double price;
    private boolean isBooked;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private MovieShow movieShow;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
