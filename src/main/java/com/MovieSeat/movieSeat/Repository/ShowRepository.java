package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.City;
import com.MovieSeat.movieSeat.Entity.MovieShow;
import com.MovieSeat.movieSeat.Entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<MovieShow,String> {
    Optional<MovieShow> findByShowId(String id);
    Optional<List<MovieShow>> findByTheatre(Theatre theatre);
    Optional<List<MovieShow>> findByCity(City city);
    Optional<List<MovieShow>> findByShowStartDate(LocalDate date);
    List<MovieShow> findByShowStartDateBefore(LocalDate date);
}
