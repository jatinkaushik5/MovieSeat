package com.MovieSeat.movieSeat.Controller;

import com.MovieSeat.movieSeat.Service.*;
import com.MovieSeat.movieSeat.io.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
public class AdminController {

    @Autowired
    CityService cityService;
    @Autowired
    TheatreService theatreService;

    @Autowired
    MovieService movieService;

    @Autowired
    ScreenService screenService;

    @Autowired
    BookingService bookingService;

    @Autowired
    showService showService;

    @PostMapping("/admin/addcity")
    public String addCity(@RequestParam("name")String name){
        return  cityService.addCity(name);
    }


    @DeleteMapping("/admin/removecity")
    public String removeCity(@RequestParam("name") String name){
       return cityService.delete(name);
    }

    @PostMapping("/admin/addMovie")
    public String addMovie(@ModelAttribute MovieRequest request){
        return movieService.create(request);
    }

    @DeleteMapping("/admin/removeMovie")
    public String removeMovie(@RequestParam("id") String id) throws IOException {
        return movieService.removeMovie(id
        );
    }

    @PatchMapping("/admin/updateMovie")
    public String updateMoveie(@ModelAttribute MovieRequest request) throws IOException {
        return movieService.updateMovie(request);
    }

    @PostMapping("/admin/addTheatre")
    public ResponseEntity<String> addTheatre(@ModelAttribute TheatreRequest request){
     return theatreService.addTheatre(request);
    }

    @PatchMapping("/admin/updateTheatre")
    public ResponseEntity<String> uddateTheatre(@ModelAttribute TheatreRequest request){
        return theatreService.updateTheatre(request);
    }

    @DeleteMapping("/admin/removeTheatre")
    public ResponseEntity<String> remove(@RequestParam("id")String id){
        System.out.println("Calling function controller");
        return theatreService.removeTheatre(id);
    }

    @GetMapping("/admin/findTheatreById")
    public ResponseEntity<Object> getbyid(@RequestParam("id")String id){
        if(theatreService.findById(id)!=null){
            return ResponseEntity.status(HttpStatus.FOUND).body(theatreService.findById(id));
        }
        else{
            Map<String,String> error=new HashMap<>();
            error.put("msg","Theatre not found with this id");
            error.put("status","Request failed");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }









    @PostMapping("/admin/addShow")
    public ResponseEntity<String> ADDshow(@ModelAttribute ShowRequest request){
        return showService.addShow(request);
    }

    @DeleteMapping("/admin/removeShow")
    public ResponseEntity<String> removeShow(@RequestParam("id")String id){
        return showService.removeShow(id);
    }

    @PatchMapping("/admin/updateShow")
    public ResponseEntity<String> updateShow(@ModelAttribute ShowRequest request){
        return showService.updateShow(request);
    }




    @PostMapping("/admin/addScreen")
    public ResponseEntity<String> addscreen(@ModelAttribute ScreenRequest request){
        if(screenService.addScreen(request)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Theatre Not found");
        }
        else{
            return ResponseEntity.status(HttpStatus.CREATED).body("Screen Saved");
        }

    }

    @DeleteMapping("/admin/removeScreen")
    public ResponseEntity<String> removescreen(@ModelAttribute ScreenRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(screenService.removeScreen(request.getCity(),request.getTheatre(),request.getName()));
    }






}
