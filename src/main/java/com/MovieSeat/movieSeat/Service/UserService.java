package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Configuration.JwtHelper;
import com.MovieSeat.movieSeat.Entity.User;
import com.MovieSeat.movieSeat.Exception.AccountExisitException;
import com.MovieSeat.movieSeat.Repository.UserRepository;
import com.MovieSeat.movieSeat.io.UserRequest;
import com.MovieSeat.movieSeat.io.UserResponse;
import com.MovieSeat.movieSeat.io.userUpdate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ImageService imageService;




    public ResponseEntity<String> createUser(UserRequest request) throws AccountExisitException {

        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Already Registered");
        }
        User user=mapper.map(request,User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(List.of("ROLE_USER"));

        userRepository.save(user);
        return  ResponseEntity.status(HttpStatus.CREATED).body("user created Successfully");
    }

    public ResponseEntity<UserResponse> updateUser(userUpdate request,HttpServletResponse response){
        Principal principal= SecurityContextHolder.getContext().getAuthentication();
        String username=principal.getName();
        User user=userRepository.findByEmail(username).orElse(null);


        if(user!=null){
            if(request.getName()!=null && !request.getName().isEmpty()){

                user.setName(request.getName());
            }
            if(request.getPassword()!=null && !request.getPassword().isEmpty()){

                Cookie cookie = new Cookie("token", null);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);

                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }


            if(request.getImage()!=null){
                try{
                    String oldid=user.getProfileName();
                    Map<String,String> data=imageService.upload(request.getImage());
                    user.setProfileName(data.get("publicId"));
                    user.setProfileUrl(data.get("url"));
                    userRepository.save(user);
                    if(oldid!=null){
                        System.out.println("Inside delete");
                        imageService.deleteImage(oldid);
                    }

                    System.out.println(user.getName());
                }
                catch (IOException e){
                   throw new RuntimeException("Some Issue Occur in Profile Change of User ");
                }

            }
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.map(user,UserResponse.class));

        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) ;
    }


    public ResponseEntity<String> DeleteUser() throws IOException {
        Principal principal= SecurityContextHolder.getContext().getAuthentication();
        String username=principal.getName();
        User user=userRepository.findByEmail(username).orElse(null);

        if(user==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Not found for deletion");
        }
        if(user.getProfileName()!=null){
            imageService.deleteImage(user.getProfileName());
        }
            userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");
    }


    public ResponseEntity<String> login(String email, String password, HttpServletResponse response) {
        User user = userRepository.findByEmail(email).orElse(null);

        if(user==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Invalid Credentials");
        }

        if(passwordEncoder.matches(password,user.getPassword())){
            String token=jwtHelper.generateToken(user);

            Cookie cookie=new Cookie("token",token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setSecure(false);
            cookie.setMaxAge(4*60*60*24);

            response.addCookie(cookie);

            return ResponseEntity.status(HttpStatus.OK).body("Login Successful");

        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Credentials");
        }




    }



}

