package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Entity.City;
import com.MovieSeat.movieSeat.Entity.Theatre;
import com.MovieSeat.movieSeat.Repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {

    @Autowired
    CityRepository cityRepository;

    public String addCity(String name){
        if(cityRepository.existsByName(name)){
            return "City alreay Present";
        }
        City city=new City();
        city.setName(name);
        cityRepository.save(city);
        return "New City: "+name+" is added";
    }

    public List<String> getAllCities(){
        List<String> cities=new ArrayList<>();
        List<City> all=cityRepository.findAll();

        for(City c:all){
            cities.add(c.getName());
        }

        return cities;
    }


    public String delete(String name){
        City city=cityRepository.findByName(name).orElse(null);
        if(city!=null){
            cityRepository.delete(city);
            return "City: "+name+" is removed";
        }else{
            return "City: "+name+" is not present";
        }
    }



}
