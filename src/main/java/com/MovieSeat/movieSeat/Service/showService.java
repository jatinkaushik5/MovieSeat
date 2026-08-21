package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Entity.*;
import com.MovieSeat.movieSeat.Repository.*;
import com.MovieSeat.movieSeat.io.MovieRequest;
import com.MovieSeat.movieSeat.io.ShowRequest;
import com.MovieSeat.movieSeat.io.ShowResponse;
import com.MovieSeat.movieSeat.io.TheatreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class showService {

    @Autowired
    ShowRepository showRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    TheatreRepository theatreRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ScreenRepository screenRepository;

    public ResponseEntity<String> addShow(ShowRequest request){
        MovieShow show=new MovieShow();
        City city=cityRepository.findByName(request.getCity()).orElse(null);
        Movie movie=movieRepository.findByName(request.getMovie()).orElse(null);
        Theatre theatre=theatreRepository.findByCityAndName(city,request.getTheatre()).orElse(null);
        Screen screen=screenRepository.findByTheatreAndName(theatre,request.getScreenName()).orElse(null);
        System.out.println("City:"+city);
        if(city==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City Not found");
        }

        if(movie==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie Not found");
        }

        if(theatre==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Theatre  Not found");
        }

        if(screen==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Screen  Not found");
        }
        show.setCity(city);
        show.setMovie(movie);

        List<ShowSeat> seats=new ArrayList<>();

        for(Seat s:screen.getSeats()){
            ShowSeat showSeat=new ShowSeat();
            showSeat.setSeat(s);
            showSeat.setMovieShow(show);
            showSeat.setBooked(false);
            seats.add(showSeat);
        }

        show.setShowSeats(seats);
        show.setTime(LocalTime.parse(request.getTime()));
        show.setScreen(screen);
        show.setShowStartDate(LocalDate.parse(request.getDate()));
        show.setTheatre(theatre);
        showRepository.save(show);
        return ResponseEntity.status(HttpStatus.CREATED).body("Show saved Successfully");
    }


    public ResponseEntity<String> removeShow(String id){
        MovieShow show=showRepository.findByShowId(id).orElse(null);

        if(show==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Such Show Present");
        }
        else{
            showRepository.delete(show);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Show Discarded");
        }
    }



    public ResponseEntity<String> updateShow(ShowRequest request){
        MovieShow show=showRepository.findByShowId(request.getId()).orElse(null);
        City city=cityRepository.findByName(request.getCity()).orElse(null);
        Movie movie=movieRepository.findByName(request.getMovie()).orElse(null);
        Theatre theatre=theatreRepository.findByCityAndName(city,request.getTheatre()).orElse(null);
        if(show==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Such Show Present");
        }
        else{
         if(request.getDate()!=null){
             show.setShowStartDate(LocalDate.parse(request.getDate()));
         }
         if(request.getTheatre()!=null  && theatre!=null){
             show.setTheatre(theatre);
         }
         if(request.getMovie()!=null && movie!=null){
             show.setMovie(movie);
         }
         if(request.getCity()!=null && city!=null){
             show.setCity(city);
         }
         if(request.getTime()!=null){

             show.setTime(LocalTime.parse(request.getTime()));
         }

         showRepository.save(show);
         return ResponseEntity.status(HttpStatus.CREATED).body("Show Updated Successfully");
        }
    }


    public List<ShowResponse> findByTheatreName(String name){
        Theatre theatre=theatreRepository.findByName(name).orElse(null);

        if(theatre!=null){
            List<MovieShow> shows=showRepository.findByTheatre(theatre).orElse(null);
            List<ShowResponse> responses=new ArrayList<>();
            if(shows!=null){
               for(MovieShow show:shows){
                   ShowResponse response=new ShowResponse();
                   response.setLocation(show.getTheatre().getLocation());
                   response.setShowId(show.getShowId());
                   response.setShowdate(show.getShowStartDate());
                   response.setTheatre(show.getTheatre().getName());
                   response.setMovie(show.getMovie().getName());
                   response.setCity(show.getCity().getName());
                   response.setTime(show.getTime());
                   responses.add(response);
               }


            }
            return responses;
        }
        else{
            return null;
        }

    }

    public List<ShowResponse>  findByCity(String city){
        City city1=cityRepository.findByName(city).orElse(null);

        if(city1!=null){
            List<MovieShow> shows=showRepository.findByCity(city1).orElse(null);
            List<ShowResponse> responses=new ArrayList<>();
            if(shows!=null){
                for(MovieShow show:shows){
                    ShowResponse response=new ShowResponse();
                    response.setLocation(show.getTheatre().getLocation());
                    response.setShowId(show.getShowId());
                    response.setShowdate(show.getShowStartDate());
                    response.setTheatre(show.getTheatre().getName());
                    response.setMovie(show.getMovie().getName());
                    response.setCity(show.getCity().getName());
                    response.setTime(show.getTime());
                    responses.add(response);
                }


            }
            return responses;
        }
        else{
            return null;
        }
    }



    public List<ShowResponse> TodayShows(){
        LocalDate date=LocalDate.now();
        List<MovieShow> shows=showRepository.findByShowStartDate(date).orElse(null);
        List<ShowResponse> responses=new ArrayList<>();
        if(shows!=null){
            for(MovieShow show:shows){
                ShowResponse response=new ShowResponse();
                response.setLocation(show.getTheatre().getLocation());
                response.setShowId(show.getShowId());
                response.setShowdate(show.getShowStartDate());
                response.setTheatre(show.getTheatre().getName());
                response.setMovie(show.getMovie().getName());
                response.setCity(show.getCity().getName());
                response.setTime(show.getTime());
                responses.add(response);
            }

            return responses;
        }

        else{
            return null;
    }

    }


    public List<ShowResponse> getall(){
        List<MovieShow> shows=showRepository.findAll();
        List<ShowResponse> responses=new ArrayList<>();
        for(MovieShow show:shows){
            ShowResponse response=new ShowResponse();
            response.setLocation(show.getTheatre().getLocation());
            response.setShowId(show.getShowId());
            response.setShowdate(show.getShowStartDate());
            response.setTheatre(show.getTheatre().getName());
            response.setMovie(show.getMovie().getName());
            response.setCity(show.getCity().getName());
            response.setTime(show.getTime());
            responses.add(response);
        }

        return responses;
    }






}
