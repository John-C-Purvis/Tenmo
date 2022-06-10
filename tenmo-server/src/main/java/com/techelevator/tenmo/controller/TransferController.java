package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/transfer")
public class TransferController {

    TransferDao transferDao;

    private List<Transfer> transfers;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @GetMapping
    public List<Transfer> getTransfers() {
        return transfers;
    }

    @GetMapping(path = "/{id}")
    public Transfer getTransferById(@PathVariable long id) {
        for(Transfer transfer : transfers) {
            if(transfer.getTransferId() == id) {
                return transfer;
            }
        }
        System.out.println("No transfer was found by the given ID");
        return null;
    }

    /*
    @GetMapping(path =
     */

    @PostMapping
    public void addTransfer(Transfer transfer) {
        if (transfer != null) {
            transferDao.createTransfer(transfer);
        }
    }
}
