package com.MovieSeat.movieSeat.io;


import com.MovieSeat.movieSeat.Entity.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatInfo {

    private String seatNumber;
    private boolean isBooked;
    private SeatType seatType;
}
