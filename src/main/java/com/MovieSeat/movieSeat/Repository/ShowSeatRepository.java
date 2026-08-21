package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.MovieShow;
import com.MovieSeat.movieSeat.Entity.Seat;
import com.MovieSeat.movieSeat.Entity.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, String> {
    List<ShowSeat> findByMovieShowId(String showId);
    Optional<ShowSeat> findBySeat(Seat seat);
    Optional<ShowSeat> findBySeatAndMovieShow(Seat seat, MovieShow movieShow);
}
