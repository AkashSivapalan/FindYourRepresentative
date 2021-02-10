package com.akash.representative.controller;

import com.akash.representative.service.GoogleClient;
import com.akash.representative.service.RepresentativeClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController

public class RepresentativeController {
    public static final String URL_GET_REPRESENTATIVE  = "/representative";

    @RequestMapping(value=URL_GET_REPRESENTATIVE, method = RequestMethod.GET)
    public ResponseEntity<String> getRepresentative(@RequestParam String address){
        GoogleClient.Coordinate coordinate = GoogleClient.getLatitudeLongitude(address);

        RepresentativeClient.Representative RepName = RepresentativeClient.getRepresentativeName(coordinate.toString());
        String name = RepName.getName();

        if (name != ""){
            return new ResponseEntity<String>(name, HttpStatus.OK);
        }
        return new ResponseEntity<String>( HttpStatus.NOT_FOUND);
    }
}
