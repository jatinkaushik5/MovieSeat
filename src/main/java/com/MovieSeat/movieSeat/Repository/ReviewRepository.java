package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.Movie;
import com.MovieSeat.movieSeat.Entity.Review;
import com.MovieSeat.movieSeat.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByMovieId(String movieId);
    List<Review> findByMovieAndUser(Movie movie, User user);
}
