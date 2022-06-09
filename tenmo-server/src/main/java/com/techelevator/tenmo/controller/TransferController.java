package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/transfer")
public class TransferController {

    private List<Transfer> transfers;

    public TransferController() {
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

    @PostMapping
    public void addTransfer(Transfer transfer) {
        if (transfer != null) {
            transfers.add(transfer);
        }
    }
}
