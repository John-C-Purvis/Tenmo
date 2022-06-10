package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AppService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AppService(String url) {
        this.baseUrl = url;
    }

    public BigDecimal getBalance(long userId) {
        return restTemplate.getForObject(baseUrl + "/account/" + userId, Account.class).getBalance();
    }
/*
public AuthenticatedUser login(UserCredentials credentials) {
        HttpEntity<UserCredentials> entity = createCredentialsEntity(credentials);
        AuthenticatedUser user = null;
        try {
            ResponseEntity<AuthenticatedUser> response =
                    restTemplate.exchange(baseUrl + "login", HttpMethod.POST, entity, AuthenticatedUser.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }
 */
}
