package com.MovieSeat.movieSeat.Exception;

public class AccountExisitException extends Exception{

    @Override
    public String getMessage() {
        return "Email Already Registered";
    }
}
