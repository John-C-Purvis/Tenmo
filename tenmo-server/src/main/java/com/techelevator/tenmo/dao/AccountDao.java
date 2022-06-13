package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    Account getAccount(long accountId);

    List<Account> getAccountsByUsernameSearch(String searchTerm);

    List<Account> getAllAccounts();

    Account createAccount(Account account);

    void updateAccount(Account account);

}
