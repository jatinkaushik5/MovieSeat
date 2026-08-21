package com.MovieSeat.movieSeat.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShowRequest {
    private String id;
    private String date;
    private String movie;
    private String screenName;
    private String city;
    private String time;
    private String theatre;
}
