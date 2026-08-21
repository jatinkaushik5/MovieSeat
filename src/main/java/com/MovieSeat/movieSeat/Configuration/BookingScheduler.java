package com.MovieSeat.movieSeat.Configuration;

import com.MovieSeat.movieSeat.Entity.Booking;
import com.MovieSeat.movieSeat.Entity.BookingStatus;
import com.MovieSeat.movieSeat.Entity.ShowSeat;
import com.MovieSeat.movieSeat.Repository.BookingRepository;
import com.MovieSeat.movieSeat.Repository.ShowSeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingScheduler {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ShowSeatRepository showSeatRepository;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void cancelExpiryBooking(){

        LocalDateTime now = LocalDateTime.now();

        List<Booking> expiredBookings =
                bookingRepository.findByStatusAndExpiresAtBefore(
                        BookingStatus.PENDING,
                        now
                );

        for (Booking booking : expiredBookings) {

            booking.setStatus(BookingStatus.CANCELLED);

            for (ShowSeat seat : booking.getBookedSeats()) {
                seat.setBooked(false);
                seat.setBooking(null);

                showSeatRepository.save(seat);
            }

            bookingRepository.save(booking);
        }

    }

}
