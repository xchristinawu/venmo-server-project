package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.User;
import org.apiguardian.api.API;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserService
{
    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/";

    private String authToken = null;

    public void setAuthToken(String authToken) { this.authToken = authToken; }


        public User[] listAllOtherUsers() {
        User[] allOtherUsers = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "/users",
                    HttpMethod.GET, makeAuthEntity(), User[].class);

            allOtherUsers = response.getBody();

        } catch (RestClientException e){
            System.out.println(e.getMessage());
            }
    return allOtherUsers;
}

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }


}
