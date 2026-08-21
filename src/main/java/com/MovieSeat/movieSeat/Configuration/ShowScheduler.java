package com.MovieSeat.movieSeat.Configuration;

import com.MovieSeat.movieSeat.Entity.MovieShow;
import com.MovieSeat.movieSeat.Repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ShowScheduler {

    @Autowired
    ShowRepository showRepository;


    @Scheduled(fixedRate = 2 * 60 * 60 * 1000)
    public void deleteExpiredShow(){
        List<MovieShow> expired=showRepository.findByShowStartDateBefore(LocalDate.now());

        if(!expired.isEmpty()){
            showRepository.deleteAll(expired);

            System.out.println(expired.size()+" expired shows deleted");
        }

    }

}
