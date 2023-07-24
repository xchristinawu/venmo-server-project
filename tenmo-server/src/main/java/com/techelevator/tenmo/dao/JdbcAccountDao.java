package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getCurrentBalance(int userId) {

        String sql = "SELECT balance FROM account WHERE user_id = ?;";

        BigDecimal results = BigDecimal.ZERO;
        try {
            results = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return results;

    }

    @Override
    public boolean sendBucks(int currentUserId, int recipientId, BigDecimal amount) {
        boolean results = false;
        String sql = "BEGIN TRANSACTION;\n" +
                "\n" +
                "UPDATE account\n" +
                "SET balance = balance - ?\n" +
                "WHERE user_id = ?;\n" +
                "\n" +
                "UPDATE account\n" +
                "SET balance = balance + ?\n" +
                "WHERE user_id = ?;\n" +
                "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (2, 2, ?, ?, ?);\n" +
                "COMMIT;";

        String accountFromSql = "SELECT account_id\n" +
                                "FROM account\n" +
                                "WHERE user_id = ?;";

        String accountToSql = "SELECT account_id\n" +
                                "FROM account\n" +
                                "WHERE user_id = ?;";

        try {
            Integer accountFrom = jdbcTemplate.queryForObject(accountFromSql, Integer.class, currentUserId);
            Integer accountTo = jdbcTemplate.queryForObject(accountToSql, Integer.class, recipientId);
            jdbcTemplate.update(sql,
                                amount,
                                currentUserId,
                                amount,
                                recipientId,
                                accountFrom,
                                accountTo,
                                amount);
            results = true;

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return results;
    }

//    public List<Transfer> viewApprovedTransfers(int accountId) {
//        List<Transfer> transferHistory = new ArrayList<>();
//
//        // how to implement?
//        // int transferStatusId = isViewingApprovedTransfers ? 2 : 1;
//
//        // viewing approved transfers only
//        String sql = "SELECT *\n" +
//                "FROM transfer\n" +
//                "WHERE (account_from = ? OR account_to = ?) AND transfer_status_id = 2;";
//
//
//        try {
//            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
//
//            while(results.next()) {
//                transferHistory.add(mapApprovedRow(results));
//            }
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        return transferHistory;
//    }

    public List<Transfer> viewTransfers(int accountId) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT t.transfer_id\n" +
                "\t, t.transfer_type_id\n" +
                "\t, t.transfer_status_id\n" +
                "\t, t.account_from\n" +
                "\t, t.account_to\n" +
                "\t, t.amount\n" +
                "\t, a.user_id as account_from_user_id\n" +
                "\t, ac.user_id as account_to_user_id\n" +
                "FROM transfer as t\n" +
                "JOIN account as a\n" +
                "\tON a.account_id = t.account_from\n" +
                "JOIN account as ac\n" +
                "\tON ac.account_id = t.account_to\n" +
                "WHERE (account_from = ? OR account_to = ?)\n" +
                "ORDER BY transfer_id ASC";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

            while(results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return transfers;
    }

    public int getUserId(String username) {
        Integer userId;
        String sql = "SELECT user_id\n" +
                "FROM tenmo_user\n" +
                "WHERE username = ?;";
        userId = jdbcTemplate.queryForObject(sql, Integer.class, username);

        return userId;
    }

    public int getAccountId(int userId) {
        Integer accountId;
        String sql = "SELECT account_id\n" +
                "FROM account\n" +
                "WHERE user_id = ?;";

        accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        return accountId;
    }

//    private Transfer mapApprovedRow(SqlRowSet sqlRowSet) {
//        int transferId = sqlRowSet.getInt("transfer_id");
//        int transferType = sqlRowSet.getInt("transfer_type_id");
//        int transferStatus = sqlRowSet.getInt("transfer_status_id");
//        int accountFrom = sqlRowSet.getInt("account_from");
//        int accountTo = sqlRowSet.getInt("account_to");
//        BigDecimal amount = sqlRowSet.getBigDecimal("amount");
//
//        String accountFromUsername = getUsernameFromAccountId(accountFrom);
//        String accountToUsername = getUsernameFromAccountId(accountTo);
//
//        Transfer transfer = new Transfer(transferId, transferType, transferStatus, amount, accountFromUsername, accountToUsername);
//
//        return transfer;
//    }

    private Transfer mapRowToTransfer(SqlRowSet sqlRowSet) {
        int transferId = sqlRowSet.getInt("transfer_id");
        int transferType = sqlRowSet.getInt("transfer_type_id");
        int transferStatus = sqlRowSet.getInt("transfer_status_id");
        int accountFrom = sqlRowSet.getInt("account_from");
        int accountTo = sqlRowSet.getInt("account_to");
        BigDecimal amount = sqlRowSet.getBigDecimal("amount");
        int userId = sqlRowSet.getInt("account_from_user_id");
        int recipientId = sqlRowSet.getInt("account_to_user_id");

        String accountFromUsername = getUsernameFromAccountId(accountFrom);
        String accountToUsername = getUsernameFromAccountId(accountTo);

        Transfer transfer = new Transfer(transferId, transferType, transferStatus, amount, accountFromUsername, accountToUsername);
        transfer.setUserId(userId);
        transfer.setRecipientId(recipientId);

        return transfer;
    }

    private String getUsernameFromAccountId(int accountId) {
        String sql = "SELECT t.username\n" +
                "FROM account as a\n" +
                "INNER JOIN tenmo_user as t\n" +
                "ON a.user_id = t.user_id\n" +
                "WHERE a.account_id = ?;";

        String username = jdbcTemplate.queryForObject(sql, String.class, accountId);

        return username;
    }

    @Override
    public boolean requestMoney(Transfer transfer) {

        boolean results = false;
        BigDecimal requestAmount = transfer.getAmount();
        int requestMoneyFromId = transfer.getRecipientId();
        int currentUserId = transfer.getUserId();

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (1, 1, ?, ?, ?);";

        String accountFromSql = "SELECT account_id\n" +
                "FROM account\n" +
                "WHERE user_id = ?;";

        String accountToSql = "SELECT account_id\n" +
                "FROM account\n" +
                "WHERE user_id = ?;";

        try {
            Integer accountFrom = jdbcTemplate.queryForObject(accountFromSql, Integer.class, requestMoneyFromId);
            Integer accountTo = jdbcTemplate.queryForObject(accountToSql, Integer.class, currentUserId);
            jdbcTemplate.update(sql,
                    accountFrom,
                    accountTo,
                    requestAmount);
            results = true;

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return results;
    }

    public void approveOrDenyPendingTransfer(int choice, Transfer transfer) {
        if (choice == 1) {
            // update sql statement
            String sql = "BEGIN TRANSACTION;\n" +
                    "\n" +
                    "UPDATE transfer\n" +
                    "SET transfer_status_id = ?\n" +
                    "WHERE transfer_id = ?;\n" +
                    "\n" +
                    "UPDATE account\n" +
                    "SET balance = balance - ?\n" +
                    "WHERE user_id = ?;\n" +
                    "UPDATE account\n" +
                    "SET balance = balance + ?\n" +
                    "WHERE user_id = ?;\n" +
                    "\n" +
                    "COMMIT;";

            try {
                jdbcTemplate.update(sql,
                        2,
                        transfer.getTransferId(),
                        transfer.getAmount(),
                        transfer.getUserId(),
                        transfer.getAmount(),
                        transfer.getRecipientId()
                        );
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if (choice == 2) {
            String sql = "UPDATE transfer\n" +
                    "SET transfer_status_id = 3\n" +
                    "WHERE transfer_id = ?;";

            try {
                jdbcTemplate.update(sql, transfer.getTransferId());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}





















