package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Entity.Cast;
import com.MovieSeat.movieSeat.Entity.Movie;
import com.MovieSeat.movieSeat.Repository.CastRepository;
import com.MovieSeat.movieSeat.Repository.MovieRepository;
import com.MovieSeat.movieSeat.io.CastResponse;
import com.MovieSeat.movieSeat.io.Castio;
import com.MovieSeat.movieSeat.io.MovieRequest;
import com.MovieSeat.movieSeat.io.MovieResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    ImageService imageService;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CastRepository castRepository;



    public String create(MovieRequest request){
        if(movieRepository.existsByName(request.getName())){
            return "Movie Already exists with this Name";
        }
        Movie movie=new Movie();
        movie.setName(request.getName());
        movie.setDescription(request.getDescription());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setLanguage(request.getLanguage());
        List<Cast> castList=new ArrayList<>();
        for(Castio c:request.getCasts()){
            Cast cast=new Cast();
            cast.setName(c.getName());
            cast.setGender(c.getGender());
            Map<String,String> data=imageService.upload(c.getPhoto());
            cast.setPhotoName(data.get("publicId"));
            cast.setPhotoUrl(data.get("url"));
            castList.add(cast);
            castRepository.save(cast);
        }
        movie.setCasts(castList);
        Map<String,String> data=imageService.upload(request.getPoster());
        movie.setPosterName(data.get("publicId"));
        movie.setPosterUrl(data.get("url"));

        movieRepository.save(movie);
        System.out.println(movie);
        return  "Movie Saved Sucessfully";
    }

    @Transactional
    public String removeMovie(String id) throws IOException {
        Movie movie=movieRepository.findByMovieId(id).orElse(null);
        if(movie!=null) {
            List<Cast> casts=movie.getCasts();
            imageService.deleteImage(movie.getPosterName());
            movieRepository.delete(movie);

            // Delete casts that have no movies left
            System.out.println("Casts"+casts);
            for (Cast cast : casts) {

                if (cast.getMovies().size()==1) {
                    System.out.println("Inside cast 1");
                    imageService.deleteImage(cast.getPhotoName());
                    castRepository.delete(cast);
                }
            }
            movie.getCasts().clear();

        }
            return "Deletion Successfull";
    }


    public String updateMovie(MovieRequest request) throws IOException {
        Movie movie=movieRepository.findByMovieId(request.getMovieId()).orElse(null);
        if(movie==null){
            return "No such Movie Exist";
        }
        else{
            if(request.getName()!=null && !request.getName().isEmpty()  ){
                movie.setName(request.getName());
            }
            if(request.getDescription()!=null&&!request.getDescription().isEmpty()){
                movie.setDescription(request.getDescription());
            }
            if(request.getLanguage()!=null&&!request.getLanguage().isEmpty()){
                movie.setLanguage(request.getLanguage());
            }
            if(request.getPoster()!=null){
                String publicId=movie.getPosterName();
                Map<String,String> data=imageService.upload(request.getPoster());
                movie.setPosterName(data.get("publicId"));
                movie.setPosterUrl(data.get("url"));

                imageService.deleteImage(publicId);
            }

            movieRepository.save(movie);
        }
        return "Movie Updated Successfully";
    }


    public List<MovieResponse> getAllMovies(){
        List<Movie> movies=movieRepository.findAll();
        List<MovieResponse> movieResponses=new ArrayList<>();

        for(Movie movie:movies){
            MovieResponse response=new MovieResponse();
            response.setMovieId(movie.getMovieId());
            response.setDescription(movie.getDescription());
            response.setLanguage(movie.getLanguage());
            response.setPoster(movie.getPosterUrl());
            response.setName(movie.getName());
            response.setRating(movie.getRating());
            response.setReleaseDate(movie.getReleaseDate());

            List<CastResponse> casts=new ArrayList<>();
            for(Cast c:movie.getCasts()){
                CastResponse response1=new CastResponse();
                response1.setGender(c.getGender());
                response1.setName(c.getName());
                response1.setPhoto(c.getPhotoUrl());
                casts.add(response1);
            }
            response.setCasts(casts);
            movieResponses.add(response);
        }
        return movieResponses;
    }


    public ResponseEntity<?> movieByMovieId(String movieId){
        Movie movie=movieRepository.findByMovieId(movieId).orElse(null);
        if(movie==null){
            return null;
        }
        MovieResponse response=new MovieResponse();
        response.setMovieId(movie.getMovieId());
        response.setDescription(movie.getDescription());
        response.setLanguage(movie.getLanguage());
        response.setPoster(movie.getPosterUrl());
        response.setName(movie.getName());
        response.setRating(movie.getRating());
        response.setReleaseDate(movie.getReleaseDate());

        return  ResponseEntity.ok(response);

    }

 }
