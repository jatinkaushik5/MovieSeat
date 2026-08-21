package com.MovieSeat.movieSeat.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Castio {
    private String name;
    private MultipartFile photo;
    private String gender;
}
