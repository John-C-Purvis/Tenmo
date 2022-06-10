package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    AccountDao accountDao;
    private List<Account> accounts;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GetMapping
    public List<Account> getAccounts() {
        return accountDao.getAllAccounts();
    }

    @GetMapping(path = "/{id}")
    public Account getAccountById(@PathVariable long id) {
        return accountDao.getAccount(id);
    }

    @PostMapping
    public void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
        }
    }
}
