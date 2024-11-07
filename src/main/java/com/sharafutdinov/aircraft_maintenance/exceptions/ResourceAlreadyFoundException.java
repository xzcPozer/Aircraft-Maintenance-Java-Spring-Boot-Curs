package com.sharafutdinov.aircraft_maintenance.exceptions;

public class ResourceAlreadyFoundException extends RuntimeException{
    public ResourceAlreadyFoundException(String message){
        super(message);
    }
}
