package com.MovieSeat.movieSeat.io;

import com.MovieSeat.movieSeat.Entity.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatResponse {

    private LocalTime time;
    private String city;
    private String theatre;
    private List<SeatInfo> seats;
}
