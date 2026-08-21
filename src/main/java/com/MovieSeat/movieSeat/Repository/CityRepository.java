package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository  extends JpaRepository<City,String> {
    boolean existsByName(String name);
    Optional<City> findByName(String name);
}
