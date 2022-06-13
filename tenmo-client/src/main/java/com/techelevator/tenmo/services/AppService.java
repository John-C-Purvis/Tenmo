package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    public BigDecimal getBalance(long userId) {
        return restTemplate.getForObject(baseUrl + "/account/" + userId, Account.class).getBalance();
    }

    public List<Account> getAccountsByUsernameSearch(String searchTerm) {
        return new ArrayList<>(Arrays.asList(restTemplate.getForObject(baseUrl + "/account/search/" + searchTerm, Account[].class)));
    }

    public User getUserByAccountId(long id) {
        return restTemplate.getForObject(baseUrl + "/users/" + id, User.class);
    }

    public Transfer createTransfer(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return restTemplate.postForObject(baseUrl + "/transfer", entity, Transfer.class);
    }

    public Account getAccountById(long userId) {
        return restTemplate.getForObject(baseUrl + "/account/" + userId, Account.class);
    }

    public void updateAccount(long accountId, Account account) {
        restTemplate.put(baseUrl + "/account/" + accountId, account);
    }
    /*
    public List<Transfer> getTransfers(long userId/accountId) {}

    public Transfer getTransferDetails(long transferId) {}

    public List<Account> getAccounts()


     */

}
