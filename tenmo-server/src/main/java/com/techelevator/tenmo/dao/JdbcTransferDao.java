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
    public List<Transfer> getAllTransfersByAccountID(long accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql =
                "SELECT transfer_id, account_from, account_to, amount, transfer_type_id, transfer_status_id " +
                        "FROM transfer " +
                        "WHERE account_to = ? OR account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        // Sending account balance update query
        String updateFromSql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        // Recipient account balance update query
        String updateToSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        // Transfer creation query
        String createTransferSql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            // Update sending account balance
            jdbcTemplate.update(
                    updateFromSql, transfer.getAmount(), transfer.getAccountFrom());
            // Update recipient account balance
            jdbcTemplate.update(
                    updateToSql, transfer.getAmount(), transfer.getAccountTo());
            // Create transfer
            long transferId = jdbcTemplate.queryForObject(createTransferSql, Long.class,
                    transfer.getTransferTypeId(),
                    transfer.getTransferStatusId(),
                    transfer.getAccountFrom(),
                    transfer.getAccountTo(),
                    transfer.getAmount());
            transfer.setTransferId(transferId);
            return transfer;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
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
