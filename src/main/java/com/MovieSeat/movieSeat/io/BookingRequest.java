package com.MovieSeat.movieSeat.io;

import com.MovieSeat.movieSeat.Entity.MovieShow;
import com.MovieSeat.movieSeat.Entity.Seat;
import com.MovieSeat.movieSeat.Entity.ShowSeat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRequest {
    private String showId;
    private List<SeatRequest> seatRequests;
}
