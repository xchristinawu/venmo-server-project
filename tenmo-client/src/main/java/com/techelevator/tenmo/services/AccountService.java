package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.util.BasicLogger;
import org.apiguardian.api.API;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/";

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal viewCurrentBalance(int userId) {
        BigDecimal currentBalance = new BigDecimal("0");

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + userId + "/balance",
                    HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            currentBalance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }

        return currentBalance;
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity(transaction, headers);
        return entity;
    }

    public boolean transferBucks(Transfer transaction) {
        boolean isSuccessful = false;
        try {
            restTemplate.put(API_BASE_URL + "/transfer",
                            makeTransferEntity(transaction));
            isSuccessful = true;
        } catch (RestClientException e){
            BasicLogger.log(e.getMessage());
        }
        return isSuccessful;
    }

    public Transfer[] viewTransferHistory() {
            Transfer[] transferHistory = null;

            try {
                    ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/history",
                            HttpMethod.GET, makeAuthEntity(), Transfer[].class);
                transferHistory = response.getBody();
            } catch (RestClientResponseException | ResourceAccessException e) {
                System.out.println(e.getMessage());
            }

            return transferHistory;
    }

    public boolean requestMoney(Transfer transaction) {
        boolean isSuccessful = false;
        try {
            restTemplate.postForObject(API_BASE_URL + "/transfer/request",
                    makeTransferEntity(transaction), Boolean.class);
            isSuccessful = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return isSuccessful;
    }

    public String approveOrDenyPendingTransfer(int choice, Transfer transfer) {
        String results = "";
        try {
            if (choice == 1) {
                // put method
                restTemplate.put(API_BASE_URL + "history/pending/approve", makeTransferEntity(transfer));
                results = "Transfer ID " + transfer.getTransferId() + " has been approved!";
            }
            if (choice == 2) {
                // delete method
                restTemplate.put(API_BASE_URL + "history/pending/deny", makeTransferEntity(transfer));
                results = "Transfer ID " + transfer.getTransferId() + " has been denied!";
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }


}
