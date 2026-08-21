package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.City;
import com.MovieSeat.movieSeat.Entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre,String> {
    Optional<Theatre> findByCity(City city);
    Optional<Theatre> findByCityAndName(City city,String name);
    Optional<Theatre> findByName(String name);
}
