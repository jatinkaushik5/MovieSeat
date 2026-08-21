package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Entity.City;
import com.MovieSeat.movieSeat.Entity.Screen;
import com.MovieSeat.movieSeat.Entity.Theatre;
import com.MovieSeat.movieSeat.Repository.CityRepository;
import com.MovieSeat.movieSeat.Repository.ScreenRepository;
import com.MovieSeat.movieSeat.Repository.TheatreRepository;
import com.MovieSeat.movieSeat.io.TheatreRequest;
import com.MovieSeat.movieSeat.io.TheatreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TheatreService {

    @Autowired
    CityRepository cityRepository;

    @Autowired
    TheatreRepository theatreRepository;

    @Autowired
    ScreenRepository screenRepository;


    public ResponseEntity<String> addTheatre(TheatreRequest request){
        Theatre theatre=new Theatre();
        City city=cityRepository.findByName(request.getCity()).orElse(null);
        if(city==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("City Not available");
        }
        if(theatreRepository.findByCityAndName(city,request.getName()).orElse(null)!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Theatre already exist with this name in "+request.getCity());
        }
        theatre.setCity(city);
        theatre.setLocation(request.getLocation());
        theatre.setName(request.getName());
        theatreRepository.save(theatre);
        return ResponseEntity.status(HttpStatus.CREATED).body("Theatre added successfully");

    }

    public ResponseEntity<String> removeTheatre(String id){
        System.out.println("Calling delete function");
        Theatre theatre=theatreRepository.findById(id).orElse(null);
        System.out.println("Theatre: "+theatre);
        if(theatre!=null){
            theatreRepository.delete(theatre);
            return ResponseEntity.status(HttpStatus.OK).body("Theatre removed successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Theatre found ");
        }
    }

    public ResponseEntity<String>  updateTheatre(TheatreRequest request){
        Theatre theatre=theatreRepository.findById(request.getId()).orElse(null);
        if(theatre==null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such Theatre exist") ;
        }
        else{
            if(request.getName()!=null){
                theatre.setName(request.getName());
            }
            if(request.getCity()!=null){
               City city1=cityRepository.findByName(request.getCity()).orElse(null);
               theatre.setCity(city1);
            }

            if(request.getLocation()!=null){
                theatre.setLocation(request.getLocation());
            }

            theatreRepository.save(theatre);
            return ResponseEntity.status(HttpStatus.OK).body("Theatre Updated Successfully");
        }

    }

    public List<TheatreResponse> getAllTheatre(){
        List<Theatre> theatreList=theatreRepository.findAll();

        List<TheatreResponse> responses=new ArrayList<>();
          for(Theatre t:theatreList){
            TheatreResponse response=new TheatreResponse();
            response.setId(t.getId());
            City city=cityRepository.findByName(t.getCity().getName()).orElse(null);
           if(city==null){
               continue;
           }
           else{
               response.setCity(city.getName());
               response.setLocation(t.getLocation());
               response.setName(t.getName());
               responses.add(response);
           }
        }

        return responses;

    }

    public List<TheatreResponse> getAllTheatresInCity(String city){
        City city1 =cityRepository.findByName(city).orElse(null);
        if(city1!=null){
            List<Theatre> theatres=city1.getTheatres();
            List<TheatreResponse> responses=new ArrayList<>();
            for(Theatre t:theatres){
                TheatreResponse response=new TheatreResponse();
                response.setName(t.getName());
                response.setCity(t.getCity().getName());
                response.setId(t.getId());
                response.setLocation(t.getLocation());
                responses.add(response);
            }
            return responses;
        }
        else{
            return null;
        }

    }

    public TheatreResponse findById(String id){
        Theatre theatre=theatreRepository.findById(id).orElse(null);
        if(theatre!=null){
            TheatreResponse response=new TheatreResponse();
            response.setId(theatre.getId());
            response.setCity(theatre.getCity().getName());
            response.setLocation(theatre.getLocation());
            response.setName(theatre.getName());
            return response;
        }
        else{
            return null;
        }
    }


    public List<String> getAllScreen(String TheatreName,String city){
        City city1=cityRepository.findByName(city).orElse(null);
        Theatre theatre=theatreRepository.findByCityAndName(city1,TheatreName).orElse(null);

        if(theatre!=null){
            List<Screen> screens=theatre.getScreens();
            List<String> screenNames=new ArrayList<>();

            for(Screen screen:screens){
                screenNames.add(screen.getName());
            }

            return screenNames;
        }
        else{
            return null;
        }

    }




}
