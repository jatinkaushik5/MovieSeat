package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.Movie;
import com.MovieSeat.movieSeat.Entity.MovieShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,String> {
    boolean existsByName(String name);
    Optional<Movie> findByMovieId(String id);
    Optional<Movie> findByName(String name);
}
