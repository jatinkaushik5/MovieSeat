package com.MovieSeat.movieSeat.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime bookingTime = LocalDateTime.now();

    private double totalAmount;
    private LocalDateTime expiresAt;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String transactionId;

    private String paymentType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private MovieShow movieShow;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<ShowSeat> bookedSeats = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;
}
