package com.MovieSeat.movieSeat.Controller;

import com.MovieSeat.movieSeat.Entity.Booking;
import com.MovieSeat.movieSeat.Entity.User;
import com.MovieSeat.movieSeat.Exception.AccountExisitException;
import com.MovieSeat.movieSeat.Repository.UserRepository;
import com.MovieSeat.movieSeat.Service.*;
import com.MovieSeat.movieSeat.io.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.Authenticator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PaymentService paymentService;


    @Autowired
    UserRepository userRepository;

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

    @PostMapping("/user/add")
    public ResponseEntity<String> create(@ModelAttribute UserRequest request) throws AccountExisitException {
        return userService.createUser(request);
    }
    @PostMapping("/user/logout")
    public ResponseEntity logout(){
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0) // Delete immediately
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }

    @PatchMapping("/user/update")
    public ResponseEntity<UserResponse> update(@ModelAttribute userUpdate request,HttpServletResponse response){
        return userService.updateUser(request,response);
    }

    @DeleteMapping("/user/remove")
    public void remove(String id) throws IOException {
        userService.DeleteUser();
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response){
        return userService.login(email,password,response);
    }

    @GetMapping("/user/me")
    public Map<String,String> getinfo(Authentication authentication){
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        if(userDetails!=null) {
            User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            String roles = userDetails.getAuthorities().iterator().next().getAuthority();

            Map<String, String> data = new HashMap<>();
            data.put("name", user.getName());
            data.put("roles", roles);

            return data;
        }
        else{
            Map<String,String> ERROR=new HashMap<>();
            ERROR.put("msg ","User not found");
            ERROR.put("status","Request Failed");
            return ERROR;
        }
    }

    @PostMapping("/user/makePayment")
    public Object make(@RequestParam("amount") long amount,Authentication authentication) throws StripeException {
        return bookingService.ConfirmBooking(authentication) ;
    }

    @GetMapping("/user/getallCities")
    public List<String> getallcity(){
        return cityService.getAllCities();
    }

    @GetMapping("/user/getallTheatre")
    public List<TheatreResponse> getall(){
        return theatreService.getAllTheatre();
    }

    @GetMapping("/user/getTheatresByCity")
    public ResponseEntity<Object> getTheatreByCity(@RequestParam("city")String city){
        if(theatreService.getAllTheatresInCity(city)!=null){
            return ResponseEntity.status(HttpStatus.FOUND).body(theatreService.getAllTheatresInCity(city));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City Not Available");
        }
    }

    @GetMapping("/user/getAllMovies")
    public List<MovieResponse> getallmovies(){
        return movieService.getAllMovies();
    }

    @GetMapping("/user/getByTheatreName")
    public List<ShowResponse> getshowByTheatreName(@RequestParam("name")String name){
        return showService.findByTheatreName(name);
    }


    @GetMapping("/user/getShowByTheatre")
    public ResponseEntity<Object> getByTheatre(@RequestParam("name")String name){
        if(showService.findByTheatreName(name)!=null){
            return ResponseEntity.status(HttpStatus.FOUND).body(showService.findByTheatreName(name));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }
    }

    @GetMapping("/user/getShowByCity")
    public ResponseEntity<?> getByCity(@RequestParam("name") String name) {

        List<ShowResponse> shows = showService.findByCity(name);

        if (shows.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No shows found for city: " + name);
        }

        return ResponseEntity.ok(shows);
    }

    @GetMapping("/user/gettodayShows")
    public ResponseEntity<Object> todayShow(){
        if(showService.TodayShows()!=null){
            return ResponseEntity.status(HttpStatus.FOUND).body(showService.TodayShows());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }

    }

    @GetMapping("/user/getallShow")
    public List<ShowResponse> GETALLShow(){
        return showService.getall();
    }

    @GetMapping("/user/getSeatInfo")
    public ResponseEntity<Object> fetchSeats(@RequestParam("id")String id){
        if(bookingService.getSeatInfo(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No show found!!");
        }
        else{
            return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getSeatInfo(id));
        }
    }

    @PostMapping("/user/requestBooking")
    public String requestBooking(@ModelAttribute BookingRequest request, Authentication authentication){
        bookingService.RequestingBookingShow(request,authentication);
        return "Booking Confirmed";
    }

    @GetMapping("/user/getAllScreen")
    public List<String> getAllScreen(@RequestParam("theatreName")String theatreName,@RequestParam("city")String city){
       return screenService.getAllScreen(theatreName,city);
    }


}
