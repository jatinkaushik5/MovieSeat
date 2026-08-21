package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.Screen;
import com.MovieSeat.movieSeat.Entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, String> {

    Optional<Screen> findByTheatreAndName(Theatre theatre,String name);
    Optional<List<Screen>> findByTheatre(Theatre theatre);
}
