package com.techelevator.tenmo.dao;

import com.techelevator.dao.BaseDaoTests;
import com.techelevator.tenmo.model.Transfer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcAccountDaoTest extends BaseDaoTests {

    private static final Transfer TRANSFER_1 = new Transfer(3001, 1, 1, new BigDecimal("100.00"),
            "user1", "user2");
    private static final Transfer TRANSFER_2 = new Transfer(3002, 1, 1, new BigDecimal("200.00"),
            "user2", "user1");

    private JdbcAccountDao sut;
    JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

//    @After
//    public void cleanup() {
//        String sql = "DELETE FROM transfer\n" +
//                "WHERE transfer_id > 3002;";
//
//        jdbcTemplate.update(sql);
//    }

    @Test
    public void viewTransfers_Should_Return_ListOfTransfers() {
        // arrange
        List<Transfer> expectedTransfers = List.of(TRANSFER_1, TRANSFER_2);

        // act
        List<Transfer> actualTransfers = sut.viewTransfers(2001);
        for (Transfer transfer : actualTransfers) {
            System.out.println(transfer.getTransferId());
        }

        // assert

        for (Transfer transfers : actualTransfers) {
            System.out.println(transfers.getAccountFrom());
            System.out.println(transfers.getAccountTo());
            System.out.println(transfers.getTransferType());
            System.out.println(transfers.getTransferStatus());
            System.out.println(transfers.getAmount());
            System.out.println(transfers.getTransferId());
        }

        String message = "Because viewTransfers should return 2 transfers for user1";
        assertEquals(message, expectedTransfers.size(), actualTransfers.size());

        message = "Because variables for each transfer should match";
        for (int i = 0; i < actualTransfers.size(); i++) {
            assertEquals(message, expectedTransfers.get(i).getTransferId(), actualTransfers.get(i).getTransferId());
            assertEquals(message, expectedTransfers.get(i).getTransferType(), actualTransfers.get(i).getTransferType());
            assertEquals(message, expectedTransfers.get(i).getTransferStatus(), actualTransfers.get(i).getTransferStatus());
            assertEquals(message, expectedTransfers.get(i).getAmount(), actualTransfers.get(i).getAmount());
            assertEquals(message, expectedTransfers.get(i).getAccountFrom(), actualTransfers.get(i).getAccountFrom());
            assertEquals(message, expectedTransfers.get(i).getAccountTo(), actualTransfers.get(i).getAccountTo());
        }
    }

    @Test
    public void getCurrentBalance_Should_Return_CorrectAmount() {
        // arrange
        BigDecimal expected = new BigDecimal("1000.00");

        // act
        BigDecimal actual = sut.getCurrentBalance(1001);

        // assert
        String message = "Because getCurrentBalance should return $1000 for userId 1001";
        assertEquals(message, expected, actual);
    }

    @Test
    public void sendBucks_Should_Update_All_Related_Tables_With_New_Amount() {
        // arrange
        String sql = "SELECT balance\n" +
                "FROM account\n" +
                "WHERE user_id = 1001";

        String sql2 = "SELECT balance\n" +
                "FROM account\n" +
                "WHERE user_id = 1002";

        String sql3 = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount\n" +
                "FROM transfer\n" +
                "WHERE account_from = 2001";

        BigDecimal expectedAmountCurrentUser = new BigDecimal("900.00");
        BigDecimal expectedAmountRecipient = new BigDecimal("1100.00");
//        Transfer expectedTransfer = new Transfer(3001, 2, 2, new BigDecimal("100.00"), "user1", "user2");

        // act
        boolean actual = sut.sendBucks(1001, 1002, new BigDecimal("100"));
        BigDecimal actualAmountCurrentUser = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        BigDecimal actualAmountRecipient = jdbcTemplate.queryForObject(sql2, BigDecimal.class);
//        Transfer actualTransfer = jdbcTemplate.queryForObject()

        // assert
        String message = "Because current user should have $900 after sending $100 to recipient";
        assertEquals(message, expectedAmountCurrentUser, actualAmountCurrentUser);
        message = "Because recipient should have $1100 after receiving $100 from current user";
        assertEquals(message, expectedAmountRecipient, actualAmountRecipient);
        message = "Because sendBucks should return true when successful";
        assertTrue(message, actual);
//        message = "Because the transfer row should contain correct information";
//        assertEquals(message, expectedTransfer.getTransferId(), actualTransfer.getTransferId());
//        assertEquals(message, expectedTransfer.getTransferType(), actualTransfer.getTransferType());
//        assertEquals(message, expectedTransfer.getTransferStatus(), actualTransfer.getTransferStatus());
//        assertEquals(message, expectedTransfer.getAmount(), actualTransfer.getAmount());
//        assertEquals(message, expectedTransfer.getAccountFrom(), actualTransfer.getAccountFrom());
//        assertEquals(message, expectedTransfer.getAccountTo(), actualTransfer.getAccountTo());
    }

    @Test
    public void getUserId_Should_Return_Correct_UserId() {
        // arrange
        int expected = 1001;

        // act
        int actual = sut.getUserId("user1");

        // assert
        String message = "Because user1 has an userId of 1001";
        assertEquals(message, expected, actual);
    }

    @Test
    public void getAccountId_Should_Return_Correct_Account() {
        // arrange
        int expected = 2002;

        // act
        int actual = sut.getAccountId(1002);

        // assert
        String message = "Because userId 1002 has an accountId of 2002";
        assertEquals(message, expected, actual);
    }

    @Test
    public void requestMoney_Should_Return_True() {
        // arrange
        Transfer transfer = new Transfer(1001, 1002, new BigDecimal("300"));
        String sqlTransferId = "SELECT transfer_id\n" +
                "FROM transfer\n" +
                "WHERE account_from = 2002 AND amount = 300";

        String sqlTransferType = "SELECT transfer_type_id\n" +
                "FROM transfer\n" +
                "WHERE account_from = 2002 AND amount = 300";

        String sqlTransferStatus = "SELECT transfer_status_id\n" +
                "FROM transfer\n" +
                "WHERE account_from = 2002 AND amount = 300";

        String sqlAccountFrom = "SELECT account_from\n" +
                "FROM transfer\n" +
                "WHERE account_from = 2002 AND amount = 300";

        String sqlAccountTo = "SELECT account_to\n" +
                "FROM transfer\n" +
                "WHERE account_from = 2002 AND amount = 300";

        String sqlAmount = "SELECT amount\n" +
                "FROM transfer\n" +
                "WHERE account_from = 2002 AND amount = 300";

        int expectedTransferId = 3003;
        int expectedTransferType = 1;
        int expectedTransferStatus = 1;
        int expectedAccountFrom = 2002;
        int expectedAccountTo = 2001;
        BigDecimal expectedAmount = new BigDecimal("300.00");

        // act
        boolean actual = sut.requestMoney(transfer);
        int actualTransferId = jdbcTemplate.queryForObject(sqlTransferId, Integer.class);
        int actualTransferType = jdbcTemplate.queryForObject(sqlTransferType, Integer.class);
        int actualTransferStatus = jdbcTemplate.queryForObject(sqlTransferStatus, Integer.class);
        int actualAccountFrom = jdbcTemplate.queryForObject(sqlAccountFrom, Integer.class);
        int actualAccountTo = jdbcTemplate.queryForObject(sqlAccountTo, Integer.class);
        BigDecimal actualAmount = jdbcTemplate.queryForObject(sqlAmount, BigDecimal.class);


        // assert
        String message = "Because requestMoney should return true if successful";
        assertTrue(message, actual);
        message = "Because all column values in transfer table should match";
        assertEquals(message, expectedTransferId, actualTransferId);
        assertEquals(message, expectedTransferType, actualTransferType);
        assertEquals(message, expectedTransferStatus, actualTransferStatus);
        assertEquals(message, expectedAccountFrom, actualAccountFrom);
        assertEquals(message, expectedAccountTo, actualAccountTo);
        assertEquals(message, expectedAmount, actualAmount);
    }

    @Test
    public void approveOrDenyPendingTransfers_Should_Execute_Users_Choice() {
        // arrange
        String sql = "SELECT transfer_status_id\n" +
                "FROM transfer\n" +
                "WHERE transfer_id = 3001";

        String sql2 = "SELECT transfer_status_id\n" +
                "FROM transfer\n" +
                "WHERE transfer_id = 3002";

        int expectedTransfer1Status = 2;
        int expectedTransfer2Status = 3;

        // act
        sut.approveOrDenyPendingTransfer(1, TRANSFER_1); // approve
        sut.approveOrDenyPendingTransfer(2, TRANSFER_2); // deny
        int actualTransfer1Status = jdbcTemplate.queryForObject(sql, Integer.class);
        int actualTransfer2Status = jdbcTemplate.queryForObject(sql2, Integer.class);

        // assert
        String message = "Transfer status should be updated to user's choice.";
        assertEquals(message, expectedTransfer1Status, actualTransfer1Status);
        assertEquals(message, expectedTransfer2Status, actualTransfer2Status);
    }

}
