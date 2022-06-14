package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account getAccount(long userId) {
        Account account = null;
        String sql =
                "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public List<Account> getAccountsByUsernameSearch(String searchTerm) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account.account_id, account.user_id, account.balance, tenmo_user.username " +
                "FROM tenmo_user " +
                "JOIN account ON account.user_id = tenmo_user.user_id " +
                "WHERE username ILIKE (?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + searchTerm + "%");
        while (results.next()) {
            accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }

    @Override
    public Account createAccount(Account account) {
        String sql = "INSERT INTO account (user_id, balance) VALUES (?, ?)  RETURNING account_id;";
        long accountId = jdbcTemplate.queryForObject(sql, Long.class, account.getUserId(), account.getBalance());
        account.setAccountId(accountId);
        return account;
    }

    @Override
    public void updateAccount(@RequestBody Account account) {
        String sql = "UPDATE account SET account_id = ?, user_id = ?, balance = ? WHERE account_id = ?;";
        try {
            jdbcTemplate.update(
                    sql, account.getAccountId(), account.getUserId(), account.getBalance(), account.getAccountId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getLong("account_id"));
        account.setUserId(results.getLong("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }

}
