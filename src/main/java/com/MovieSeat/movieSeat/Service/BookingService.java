package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Entity.*;
import com.MovieSeat.movieSeat.Repository.BookingRepository;
import com.MovieSeat.movieSeat.Repository.SeatRepository;
import com.MovieSeat.movieSeat.Repository.ShowRepository;
import com.MovieSeat.movieSeat.Repository.ShowSeatRepository;
import com.MovieSeat.movieSeat.Repository.UserRepository;
import com.MovieSeat.movieSeat.io.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class BookingService {

    @Autowired
    ShowRepository showRepository;

    @Autowired
    ShowSeatRepository showSeatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    BookingRepository bookingRepository;

    public SeatResponse getSeatInfo(String showId){
        MovieShow show=showRepository.findByShowId(showId).orElse(null);

        if(show==null){
            return  null;
        }
        else{
            SeatResponse response=new SeatResponse();
            response.setCity(show.getCity().getName());
            response.setTime(show.getTime());
            response.setTheatre(show.getTheatre().getName());
            List<SeatInfo> seats=new ArrayList<>();
            System.out.println(show.getShowSeats());
            List<ShowSeat> silverSeats = show.getShowSeats().stream()
                    .filter(showSeat -> showSeat.getSeat().getSeatType() == SeatType.SILVER)
                    .sorted(Comparator.comparing((ShowSeat s)->s.getSeat().getRowLabel()).thenComparing(s->s.getSeat().getSeatNumber())
                    ).toList();
            List<ShowSeat> platinumSeat = show.getShowSeats().stream()
                    .filter(showSeat -> showSeat.getSeat().getSeatType() == SeatType.PLATINUM)
                    .sorted(Comparator.comparing((ShowSeat s)->s.getSeat().getRowLabel()).thenComparing(s->s.getSeat().getSeatNumber()))
                    .toList();
            List<ShowSeat> goldSeat = show.getShowSeats().stream()
                    .filter(showSeat -> showSeat.getSeat().getSeatType() == SeatType.GOLD)
                    .sorted(Comparator.comparing((ShowSeat s)->s.getSeat().getRowLabel()).thenComparing(s->s.getSeat().getSeatNumber()))
                    .toList();

            for(ShowSeat show1:platinumSeat){
                SeatInfo info=new SeatInfo();
                info.setBooked(show1.isBooked());
                info.setSeatType(show1.getSeat().getSeatType());
                info.setSeatNumber(show1.getSeat().getRowLabel()+show1.getSeat().getSeatNumber());
                seats.add(info);
            }

            for(ShowSeat show1:goldSeat){
                SeatInfo info=new SeatInfo();
                info.setBooked(show1.isBooked());
                info.setSeatType(show1.getSeat().getSeatType());
                info.setSeatNumber(show1.getSeat().getRowLabel()+show1.getSeat().getSeatNumber());
                seats.add(info);
            }
            for(ShowSeat show1:silverSeats){
                SeatInfo info=new SeatInfo();
                info.setBooked(show1.isBooked());
                info.setSeatType(show1.getSeat().getSeatType());
                info.setSeatNumber(show1.getSeat().getRowLabel()+show1.getSeat().getSeatNumber());
                seats.add(info);
            }

            response.setSeats(seats);

            return response;
        }
    }



    @Transactional
    public void RequestingBookingShow(BookingRequest request, Authentication authentication){
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        MovieShow show = showRepository.findByShowId(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + request.getShowId()));

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        booking.setPayment(null);
        booking.setMovieShow(show);
        booking.setUser(user);

        // Save booking first so it is persistent and not transient
        booking = bookingRepository.save(booking);

        List<ShowSeat> seats = new ArrayList<>();

        for(SeatRequest request1 : request.getSeatRequests()){
            String seatNumberStr = request1.getSeatNumber();
            if(seatNumberStr == null || seatNumberStr.length() < 2){
                throw new RuntimeException("Invalid seat number format: " + seatNumberStr);
            }

            String rowLabel = String.valueOf(seatNumberStr.charAt(0));
            int seatNumber = Integer.parseInt(seatNumberStr.substring(1));

            // Use the screen-scoped query to retrieve the unique seat
            Seat seat = seatRepository.findByRowLabelAndSeatNumberAndScreen(rowLabel, seatNumber, show.getScreen())
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNumberStr));

            ShowSeat showSeat = showSeatRepository.findBySeatAndMovieShow(seat, show)
                    .orElseThrow(() -> new RuntimeException("Show seat not found for seat: " + seatNumberStr));

            if(showSeat.isBooked()){
                throw new RuntimeException("Seat " + seatNumberStr + " is already booked");
            }

            switch (request1.getSeatType()){
                case "PLATINUM":
                    showSeat.setPrice(300);
                    break;
                case "SILVER":
                    showSeat.setPrice(500);
                    break;
                case "GOLD":
                    showSeat.setPrice(700);
                    break;
                default:
                    throw new RuntimeException("Invalid seat type: " + request1.getSeatType());
            }
            showSeat.setBooked(true);
            showSeat.setBooking(booking);
            seats.add(showSeat);
        }

        booking.setBookedSeats(seats);

        int totalPrice = 0;
        for(ShowSeat seat : seats){
            totalPrice += seat.getPrice();
        }
        booking.setTotalAmount(totalPrice);
        booking.setExpiresAt(LocalDateTime.now().plusMinutes(3));
        bookingRepository.save(booking);
    }

    public Object ConfirmBooking(Authentication authentication) throws StripeException {
        Principal principal= SecurityContextHolder.getContext().getAuthentication();
        String username=principal.getName();
        User user=userRepository.findByEmail(username).orElse(null);

        if(user!=null){
            Booking booking=bookingRepository.findTopByUserOrderByBookingTimeDesc(user).orElse(null);
            if(booking!=null){
                PaymentService paymentService=new PaymentService();
                PaymentIntent paymentIntent =paymentService.createPayment((long) booking.getTotalAmount());

                PaymentIntent paymentResponse=PaymentIntent.retrieve(paymentIntent.getId());

                PaymentIntentConfirmParams params=PaymentIntentConfirmParams.builder()

                        .setPaymentMethod("pm_card_visa").build();

                PaymentIntent finalResponse=paymentResponse.confirm(params);
                if(finalResponse.getStatus().equals("succeeded")){
                    booking.setStatus(BookingStatus.CONFIRMED);
                    booking.setTransactionId(paymentIntent.getId());
                    booking.setPaymentType("UPI");
                    bookingRepository.save(booking);
                }
                else{
                    return "Payment Failed";
                }
            }
            else{
                return "No recent Booking Found";
            }
        }
        else{
            return "User Not found!!";
        }
        return "Booking Confirmed";
    }


    public BookingResponse getLatestBookingByUser(){
        Principal principal=SecurityContextHolder.getContext().getAuthentication();
        String username=principal.getName();
        User user=userRepository.findByEmail(username).orElse(null);
        Booking booking=bookingRepository.findTopByUserOrderByBookingTimeDesc(user).orElse(null);

        if(booking!=null){
            BookingResponse response=new BookingResponse();
            response.setUser(user.getName());
            response.setEmail(user.getEmail());
            ShowResponse showResponse=new ShowResponse();
            showResponse.setShowdate(booking.getMovieShow().getShowStartDate());
            showResponse.setMovie(booking.getMovieShow().getMovie().getName());
            showResponse.setShowId(booking.getMovieShow().getShowId());
            showResponse.setLocation(booking.getMovieShow().getTheatre().getLocation());
            showResponse.setCity(booking.getMovieShow().getTheatre().getCity().getName());
            showResponse.setTime(booking.getMovieShow().getTime());
            showResponse.setTheatre(booking.getMovieShow().getTheatre().getName());
            response.setShow(showResponse);
            return response;
        }
        else{
            return  null;
        }
    }


    public List<BookingResponse> getAllBookingsByUser(){
        Principal principal=SecurityContextHolder.getContext().getAuthentication();
        String username=principal.getName();
        User user=userRepository.findByEmail(username).orElse(null);
        List<BookingResponse> responses=new ArrayList<>();
        List<Booking> bookings=bookingRepository.findByUserId(user.getUserId());
        for(Booking booking:bookings){
            BookingResponse response=new BookingResponse();
            response.setUser(user.getName());
            response.setEmail(user.getEmail());
            ShowResponse showResponse=new ShowResponse();
            showResponse.setShowdate(booking.getMovieShow().getShowStartDate());
            showResponse.setMovie(booking.getMovieShow().getMovie().getName());
            showResponse.setShowId(booking.getMovieShow().getShowId());
            showResponse.setLocation(booking.getMovieShow().getTheatre().getLocation());
            showResponse.setCity(booking.getMovieShow().getTheatre().getCity().getName());
            showResponse.setTime(booking.getMovieShow().getTime());
            showResponse.setTheatre(booking.getMovieShow().getTheatre().getName());
            response.setShow(showResponse);

            responses.add(response);
        }

        return responses;
    }












}
