package com.MovieSeat.movieSeat.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieRequest {
    private String movieId;
    private String name;
    private MultipartFile poster;
    private String description;
    private LocalDate releaseDate;
    private String language;
    private List<Castio> casts;
}
