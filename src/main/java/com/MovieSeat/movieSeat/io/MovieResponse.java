package com.MovieSeat.movieSeat.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieResponse {
    private String movieId;
    private String name;
    private String poster;
    private Double rating;
    private String description;
    private LocalDate releaseDate;
    private String language;

    private List<CastResponse> casts=new ArrayList<>();

}


