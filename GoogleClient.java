package com.akash.representative.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

public class GoogleClient {

    private static final String GOOGLE_URL = "https://maps.googleapis.com";

    public static Coordinate getLatitudeLongitude(String address){
        WebClient wc = WebClient.create(GOOGLE_URL);
        String googleResponseJson = wc.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maps/api/geocode/json")
                        .queryParam("address", address)
                        .queryParam("key","AIzaSyDVT27ynaH-Exh_YE6g3p54muKITjymgtk")
                        .build())
                .exchange().block().bodyToMono(String.class).block();
        String latitude = getLatitude(googleResponseJson);
        String longitude = getLongitude(googleResponseJson);
        return new Coordinate(longitude,latitude);
    }

// TODO: Extract ObjectMapper out
    public static String getLatitude(String response){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response);
            JsonNode latitudeNode = node.at("/results/0/geometry/location/lat");
            return latitudeNode.asText();
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getLongitude(String response){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response);
            JsonNode longitudeNode = node.at("/results/0/geometry/location/lng");
            return longitudeNode.asText();
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "";
    }

    public static class Coordinate{
        String longitude;
        String latitude;

        public Coordinate(String longitude, String latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
        public String getLongitude() {
            return longitude;
        }
        public String getLatitude() {
            return latitude;
        }
        public String toString(){
            return latitude +", "+ longitude;
        }
    }

}

