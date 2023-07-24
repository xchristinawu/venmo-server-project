package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountServiceTest
{
//    private static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal("1000"));
//    private static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal("900"));
//    private static final Account ACCOUNT_3 = new Account(2003, 1003, new BigDecimal("1500"));
//
//    private AccountService accountService;
//    private User user1 = new User();
//    private AuthenticatedUser currentUser = new AuthenticatedUser();
//    private UserService userService;
//
//    @Before
//    public void setup() {
//        accountService = new AccountService();
//        userService = new UserService();
//        user1.setId(1001);
//        user1.setUsername("christina");
//        currentUser.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaHJpc3RpbmEiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjcyMzUxMTU2fQ.wCI0bZSrOINr6b0r6xPksIhySib8yuNX_i0_zcLC4QwhkBg-R3jjQaoEwiFx92ePJv2drWo52ovNmvBM1V2YWw");
//        currentUser.setUser(user1);
//        accountService.setAuthToken(currentUser.getToken());
//        userService.setAuthToken(currentUser.getToken());
//    }
//
//    @Test
//    public void viewCurrentBalance_Should_Return_Balance_Using_UserId()
//    {
//        //arrange
//        BigDecimal expected = new BigDecimal("1000");
//
//        // act
//        BigDecimal actual = accountService.viewCurrentBalance(1001);
//
//        //assert
//        String message = "Because viewCurrentBalance should return the correct balance using userId.";
//        assertEquals(message, expected, actual);
//
//
//    }
//
//    @Test
//    public void transferBucks()
//    {
//        // arrange
//        Transfer transfer = new Transfer(3001, 2, 2, new BigDecimal("100"), "christina", "cat");
//
//        // act
//        boolean actual = accountService.transferBucks(transfer);
//
//        // assert
//        String message = "Because transferBucks should return true when called.";
//        assertTrue(message, actual);
//    }
}
