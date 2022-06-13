package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AccountController {

    AccountDao accountDao;
    UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/account")
    public List<Account> getAccounts() {
        return accountDao.getAllAccounts();
    }

    @GetMapping(path = "/account/{id}")
    public Account getAccountById(@PathVariable long id) {
        return accountDao.getAccount(id);
    }

    @GetMapping(path = "/account/search/{searchTerm}")
    public List<Account> getAccountsByUsernameSearch(@PathVariable String searchTerm) {
        return accountDao.getAccountsByUsernameSearch(searchTerm);
    }

    @GetMapping(path = "/users/{id}")
    public User getUserByAccountId(@PathVariable long id) {
        return userDao.findUserByAccountId(id);
    }

    @PutMapping(path = "/users/{id}")
    public boolean updateAccount(@PathVariable long id, Account account) {
        return accountDao.updateAccount(account);
    }
}
