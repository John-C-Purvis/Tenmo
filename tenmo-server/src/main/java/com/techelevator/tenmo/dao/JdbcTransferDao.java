package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Transfer getTransfer(long transferId) {
        Transfer transfer = null;
        String sql =
                "SELECT transfer_id, account_from, account_to, amount, transfer_type_id, transfer_status_id " +
                        "FROM transfer " +
                        "WHERE transfer.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToAccount(results);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfersByAccountID(long accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql =
                "SELECT transfer_id, account_from, account_to, amount, transfer_type_id, transfer_status_id " +
                "FROM transfer " +
                "WHERE account_to = ? OR account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (results.next()) {
            transfers.add(mapRowToAccount(results));
        }
        return transfers;
    }

    @Override
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        long transferId = jdbcTemplate.queryForObject(sql, Long.class,
                transfer.getTransferTypeId(),
                transfer.getTransferStatusId(),
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getAmount()
        );
        transfer.setTransferId(transferId);
        return transfer;
    }

    @Override
    public boolean updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer SET " +
                "transfer_type_id = ?, " +
                "transfer_status_id = ?, " +
                "account_from = ?, " +
                "account_to = ? " +
                "amount = ? " +
                "WHERE account_id = ?;";
        int numberOfRows = jdbcTemplate.update(sql,
                transfer.getTransferTypeId(),
                transfer.getTransferStatusId(),
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getAmount(),
                transfer.getTransferId()
        );
        return numberOfRows == 1;
    }

    // May have to change the set methods / variables.
    private Transfer mapRowToAccount(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }

}
