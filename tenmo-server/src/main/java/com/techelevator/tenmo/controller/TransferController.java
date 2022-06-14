//package com.techelevator.tenmo.controller;
//
//import com.techelevator.tenmo.dao.JdbcTransferDao;
//import com.techelevator.tenmo.dao.TransferDao;
//import com.techelevator.tenmo.model.Account;
//import com.techelevator.tenmo.model.Transfer;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(path = "/transfer")
//public class TransferController {
//
//    TransferDao transferDao;
//
//    public TransferController(TransferDao transferDao) {
//        this.transferDao = transferDao;
//    }
//
//    @GetMapping
//    public List<Transfer> getTransfers() {
//        return null;
//    }
//
//    @GetMapping(path = "/{id}")
//    public Transfer getTransferById(@PathVariable long id) {
//        Transfer transfer = null;
//        try {
//            transfer = transferDao.getTransfer(id);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return transfer;
//    }
//
//    @PostMapping
//    public Transfer addTransfer(@RequestBody Transfer transfer) {
//        if (transfer != null) {
//            return transferDao.createTransfer(transfer);
//        }
//        return null;
//    }
//}
