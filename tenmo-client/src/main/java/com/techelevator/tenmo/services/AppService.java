package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AppService(String url) {
        this.baseUrl = url;
    }

    public BigDecimal getBalance(long userId, String token) {
        HttpEntity<Void> entity = makeAuthEntity(token);
        return restTemplate.exchange(
                baseUrl + "/account/" + userId, HttpMethod.GET, entity, Account.class).getBody().getBalance();
    }

    public List<Account> getAccountsByUsernameSearch(String searchTerm, String token) {
        HttpEntity<Void> entity = makeAuthEntity(token);
        return new ArrayList<>(Arrays.asList(
                restTemplate.exchange(
                        baseUrl + "/account/search/" + searchTerm,
                        HttpMethod.GET, entity, Account[].class).getBody()));
    }

    public User getUserByAccountId(long id, String token) {
        HttpEntity<Void> entity = makeAuthEntity(token);
        return restTemplate.exchange(baseUrl + "/users/" + id, HttpMethod.GET, entity, User.class).getBody();
    }

    public Account getAccountById(long userId, String token) {
        HttpEntity<Void> entity = makeAuthEntity(token);
        return restTemplate.exchange(
                baseUrl + "/account/" + userId, HttpMethod.GET, entity, Account.class).getBody();
    }

    public void updateAccount(long accountId, Account account, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        restTemplate.put(baseUrl + "/account/" + accountId, entity);
    }

    public Transfer createTransfer(Transfer transfer, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return restTemplate.postForObject(baseUrl + "/transfer", entity, Transfer.class);
    }

    public List<Transfer> getTransfersByAccountId(long id, String token) {
        HttpEntity<Void> entity = makeAuthEntity(token);
        return new ArrayList<>(Arrays.asList(
                restTemplate.exchange(
                        baseUrl + "/transfer/account/" + id,
                        HttpMethod.GET, entity, Transfer[].class).getBody()));
    }

    private HttpEntity<Void> makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
