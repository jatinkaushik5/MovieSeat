package com.MovieSeat.movieSeat.Repository;

import com.MovieSeat.movieSeat.Entity.Booking;
import com.MovieSeat.movieSeat.Entity.BookingStatus;
import com.MovieSeat.movieSeat.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUserId(String userId);

    Optional<Booking> findTopByUserOrderByBookingTimeDesc(User user);

    List<Booking> findByStatusAndExpiresAtBefore(
            BookingStatus status,
            LocalDateTime time
    );
}
