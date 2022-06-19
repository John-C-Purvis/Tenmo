package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Account getAccount(long userId);

    BigDecimal getAccountBalance(long accountId);

    List<Long> getAccountIdsByUsernameSearch(String searchTerm);

}
