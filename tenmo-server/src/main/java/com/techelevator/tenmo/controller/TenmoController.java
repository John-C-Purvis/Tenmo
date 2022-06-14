package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    AccountDao accountDao;
    UserDao userDao;
    TransferDao transferDao;

    public TenmoController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }


    /*
     * AccountDao methods
     */

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

    @PutMapping(path = "/account/{id}")
    public void updateAccount(@PathVariable long id, @RequestBody Account account) {
        accountDao.updateAccount(account);
    }


    /*
     * UserDao methods
     */

    @GetMapping(path = "/users/{id}")
    public User getUserByAccountId(@PathVariable long id) {
        return userDao.findUserByAccountId(id);
    }


    /*
     * TransferDao methods
     */

    @GetMapping(path = "/transfer/account/{accountId}")
    public List<Transfer> getTransfers(@PathVariable long accountId) {
        return transferDao.getAllTransfersByAccountID(accountId);
    }

    @GetMapping(path = "/transfer/{id}")
    public Transfer getTransferById(@PathVariable long id) {
        Transfer transfer = null;
        try {
            transfer = transferDao.getTransfer(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    @PostMapping(path = "/transfer")
    public Transfer addTransfer(@RequestBody Transfer transfer) {
        if (transfer != null) {
            return transferDao.createTransfer(transfer);
        }
        return null;
    }
}
