package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getCurrentBalance(int userId);
    int getUserId(String username);
    int getAccountId(int userId);

    boolean sendBucks(int currentUserId, int recipientId, BigDecimal amount);
//    List<Transfer> viewApprovedTransfers(int accountId);
    List<Transfer> viewTransfers(int accountId);
    boolean requestMoney(Transfer transfer);
    void approveOrDenyPendingTransfer(int choice, Transfer transfer);

}
