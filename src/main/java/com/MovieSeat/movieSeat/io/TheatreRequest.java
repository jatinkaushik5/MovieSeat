package com.MovieSeat.movieSeat.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TheatreRequest {
    private String id;
    private String name;
    private String location;
    private String city;
}
