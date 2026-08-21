package com.MovieSeat.movieSeat.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.pl.NIP;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatRequest {
    private String seatType;
    private String seatNumber;
}
