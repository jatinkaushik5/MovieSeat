package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Entity.Movie;
import com.MovieSeat.movieSeat.Entity.Review;
import com.MovieSeat.movieSeat.Entity.User;
import com.MovieSeat.movieSeat.Repository.MovieRepository;
import com.MovieSeat.movieSeat.Repository.ReviewRepository;
import com.MovieSeat.movieSeat.Repository.UserRepository;
import com.MovieSeat.movieSeat.io.ReviewRequest;
import com.MovieSeat.movieSeat.io.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReviewService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    public ResponseEntity<?> addReview(ReviewRequest request){
        Review review=new Review();
        Movie movie=movieRepository.findByName(request.getMovieName()).orElse(null);

        if(movie==null){
            return ResponseEntity.badRequest().body("Movie not found");
        }

        Principal principal= SecurityContextHolder.getContext().getAuthentication();
        String username=principal.getName();
        User user=userRepository.findByEmail(username).orElse(null);
        if(user==null){
            return ResponseEntity.badRequest().body("Invalid user Request");
        }

        review.setComment(request.getComment());
        review.setRating(request.getRating());
        review.setMovie(movie);
        review.setUser(user);

        reviewRepository.save(review);
        return ResponseEntity.ok("Review Added");
    }


    public ResponseEntity<?> getReviewsofMovie(String movieName){
        Movie movie=movieRepository.findByName(movieName).orElse(null);

        if(movie==null){
            return ResponseEntity.badRequest().body("No such Movie present");
        }

        List<Review> reviewList= reviewRepository.findByMovieId(movie.getMovieId());

        List<ReviewResponse> responses=new ArrayList<>();

        for(Review review:reviewList){
            ReviewResponse response=new ReviewResponse();
            response.setComment(review.getComment());
            response.setRating(review.getRating());
            response.setUsername(review.getUser().getUsername());
            responses.add(response);
        }

        return ResponseEntity.ok(responses);
    }


    public  ResponseEntity<?> deleteReviewsByUser(String movieName){
        Movie movie=movieRepository.findByName(movieName).orElse(null);
        Principal principal=SecurityContextHolder.getContext().getAuthentication();
        String username=principal.getName();
        User user=userRepository.findByEmail(username).orElse(null);
        if(movie==null){
            return ResponseEntity.badRequest().body("No such Movie present");
        }
        if(user==null){
            return ResponseEntity.badRequest().body("Invalid user Request");
        }

        List<Review> reviewList=reviewRepository.findByMovieAndUser(movie,user);

        if(reviewList.size()==0){
            return ResponseEntity.ok("No Review from the user present");
        }
        else{
            for(Review review:reviewList){
                reviewRepository.delete(review);
            }

            return ResponseEntity.ok("All Review  deleted");
        }
    }

    public ResponseEntity<?> deleteReviewById(String id){
        Review review=reviewRepository.findById(id).orElse(null);

        if(review==null){
            return ResponseEntity.badRequest().body("No Review Found");
        }
        else{
            reviewRepository.delete(review);
            return ResponseEntity.ok("Review Removed");
        }
    }




}
