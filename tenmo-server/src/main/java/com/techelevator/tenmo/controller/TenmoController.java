package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
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

    @GetMapping(path = "/account/{id}")
    public Account getAccountByUserId(@PathVariable long id, Principal principal)
            throws AccessDeniedException {
        if (principal.getName().equals(getUsernameByAccountId(
                accountDao.getAccount(id).getAccountId()))){
            return accountDao.getAccount(id);
        }
        throw new AccessDeniedException("Access Denied");
    }

    @GetMapping(path = "/account/search/{searchTerm}")
    public List<Long> getAccountIdsByUsernameSearch(@PathVariable String searchTerm) {
        return accountDao.getAccountIdsByUsernameSearch(searchTerm);
    }


    /*
     * UserDao methods
     */

    @GetMapping(path = "/users/{id}")
    public String getUsernameByAccountId(@PathVariable long id) {
        return userDao.findUsernameByAccountId(id);
    }


    /*
     * TransferDao methods
     */

    @GetMapping(path = "/transfer/account/{accountId}")
    public List<Transfer> getTransfers(@PathVariable long accountId, Principal principal)
            throws AccessDeniedException {
        if (principal.getName().equals(
                getUsernameByAccountId(accountId))){
            return transferDao.getAllTransfersByAccountID(accountId);
        }
        throw new AccessDeniedException("Access denied");
    }

    @PostMapping(path = "/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer addTransfer(@RequestBody Transfer transfer, Principal principal)
            throws AccessDeniedException{
        if (principal.getName().equals(getUsernameByAccountId(transfer.getAccountFrom()))
                && transfer.getAccountTo() != transfer.getAccountFrom()
                && transfer.getAmount().compareTo(
                accountDao.getAccountBalance(transfer.getAccountFrom())) <= 0
                && transfer.getTransferTypeId() == 2
                && transfer.getTransferStatusId() == 2
                && transfer.getAmount().compareTo(new BigDecimal("0")) > 0) {
            return transferDao.createTransfer(transfer);
        }
        throw new AccessDeniedException("Access Denied");
    }
}
