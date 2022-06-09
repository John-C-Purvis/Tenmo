package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransfer(int transferId);

    List<Transfer> getAllTransfers();

    Transfer createTransfer(Transfer transfer);

    boolean updateTransfer(Transfer transfer);

}
