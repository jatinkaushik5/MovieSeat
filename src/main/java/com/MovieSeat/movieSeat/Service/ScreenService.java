package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Entity.City;
import com.MovieSeat.movieSeat.Entity.Screen;
import com.MovieSeat.movieSeat.Entity.Theatre;
import com.MovieSeat.movieSeat.Repository.CityRepository;
import com.MovieSeat.movieSeat.Repository.ScreenRepository;
import com.MovieSeat.movieSeat.Repository.TheatreRepository;
import com.MovieSeat.movieSeat.io.ScreenRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ScreenService {

    @Autowired
    ScreenRepository screenRepository;
    @Autowired
    TheatreRepository theatreRepository;

    @Autowired
    CityRepository cityRepository;

    public String addScreen(ScreenRequest request){
        City city=cityRepository.findByName(request.getCity()).orElse(null);
        Theatre theatre=theatreRepository.findByCityAndName(city,request.getTheatre()).orElse(null);
        if(theatre!=null){
            return null;
        }
        else {
            Screen screen = new Screen();
            screen.setName(request.getName());
            screen.setTheatre(theatre);
            screenRepository.save(screen);
            return "Screen saved Successfully";
        }
    }

    public String removeScreen(String city,String theatreName,String screenName){
        City city1=cityRepository.findByName(city).orElse(null);
        if(city1!=null){
            Theatre theatre=theatreRepository.findByCityAndName(city1,theatreName).orElse(null);

            if(theatre!=null){
                Screen screen=screenRepository.findByTheatreAndName(theatre,screenName).orElse(null);
                System.out.println(screen);
                if(screen==null){
                    return "Screen not found";
                }
                else{
                    screenRepository.delete(screen);
                    return "Screen Removed Successfully";
                }
            }
            else{
                return "Theatre Not found";
            }

        }
        else{
            return "City not exist";
        }
    }

    public List<String> getAllScreen(String theatreName,String city){
        List<String> screenNames=new ArrayList<>();
        City city1=cityRepository.findByName(city).orElse(null);
        Theatre theatre=theatreRepository.findByCityAndName(city1,theatreName).orElse(null);
        List<Screen> screens=screenRepository.findByTheatre(theatre).orElse(null);
        for(Screen screen:screens){
            screenNames.add(screen.getName());
        }

        return screenNames;
    }





}
