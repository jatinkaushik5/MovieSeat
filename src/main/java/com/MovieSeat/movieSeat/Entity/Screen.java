package com.MovieSeat.movieSeat.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("rowLabel ASC, seatNumber ASC")
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieShow> shows = new ArrayList<>();

    @PrePersist
    private void generateSeats() {

        if (!seats.isEmpty()) return;

        addRow("A", 8, SeatType.PLATINUM);
        addRow("B", 8, SeatType.PLATINUM);

        addRow("C", 10, SeatType.GOLD);
        addRow("D", 10, SeatType.GOLD);
        addRow("E", 10, SeatType.GOLD);
        addRow("F", 10, SeatType.GOLD);

        addRow("G", 10, SeatType.SILVER);
        addRow("H", 10, SeatType.SILVER);
        addRow("I", 10, SeatType.SILVER);
        addRow("J", 10, SeatType.SILVER);
    }

    private void addRow(String row, int totalSeats, SeatType type) {

        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setRowLabel(row);
            seat.setSeatNumber(i);
            seat.setSeatType(type);
            seat.setScreen(this);

            seats.add(seat);
        }
    }
}
