package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.Screen;
import com.MovieSeat.movieSeat.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {

    Optional<Seat> findByRowLabelAndSeatNumber(String row, int number);
    Optional<Seat> findByRowLabelAndSeatNumberAndScreen(String row, int number, Screen screen);
}
