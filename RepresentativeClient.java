package com.akash.representative.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

public class RepresentativeClient {

    private static final String REPRESENTATIVE_URL = "https://represent.opennorth.ca";

    public static Representative getRepresentativeName(String coordinates){
        WebClient rc =WebClient.create(REPRESENTATIVE_URL);
        String representNorthResponseJson =rc.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/representatives/house-of-commons/")
                        .queryParam("point",coordinates)
                        .build())
                .exchange().block().bodyToMono(String.class).block();
        Representative representative = new Representative(getRepresentative(representNorthResponseJson));
        return representative;
    }

    public static String getRepresentative(String response){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response);
            JsonNode representativeNameNode = node.at("/objects/0/name");
            return representativeNameNode.asText();
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "";
    }

    public static class Representative {
        String name;

        public Representative(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
}
