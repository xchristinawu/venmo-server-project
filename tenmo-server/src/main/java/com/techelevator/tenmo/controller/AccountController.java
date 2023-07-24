package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    // constructor
    public AccountController(JdbcAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getCurrentBalance(@PathVariable int id) {
        return accountDao.getCurrentBalance(id);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.PUT)
    public boolean transferBucks(@RequestBody Transfer transaction) {
        int userId= transaction.getUserId();
        int recipientId = transaction.getRecipientId();
        return accountDao.sendBucks(transaction.getUserId(), transaction.getRecipientId(), transaction.getAmount());
    }

//    @RequestMapping(path = "/history", method = RequestMethod.GET)
//    public List<Transfer> viewTransferHistory(Principal principal) {
//        String username = principal.getName();
//        int userId = accountDao.getUserId(username);
//        int accountId = accountDao.getAccountId(userId);
//
//        return accountDao.viewApprovedTransfers(accountId);
//    }

    @RequestMapping(path = "/history", method = RequestMethod.GET)
    public List<Transfer> viewPendingHistory(Principal principal) {
        String username = principal.getName();
        int userId = accountDao.getUserId(username);
        int accountId = accountDao.getAccountId(userId);

        return accountDao.viewTransfers(accountId);
    }

    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public boolean requestMoney(@RequestBody Transfer transaction) {
        return accountDao.requestMoney(transaction);
    }

    @RequestMapping(path="/history/pending/approve", method = RequestMethod.PUT)
    public void approvePendingTransfer(@RequestBody Transfer transfer) {
        accountDao.approveOrDenyPendingTransfer(1, transfer);
    }

    //@PreAuthorize("permitAll()")
    @RequestMapping(path="/history/pending/deny", method = RequestMethod.PUT)
    public void denyPendingTransfer(@RequestBody Transfer transfer) {
        accountDao.approveOrDenyPendingTransfer(2, transfer);
    }
}
