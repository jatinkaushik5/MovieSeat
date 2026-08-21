package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.Cast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastRepository extends JpaRepository<Cast,String> {
}
