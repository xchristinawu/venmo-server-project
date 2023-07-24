package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.views.UserOutput;

import java.math.BigDecimal;


public class TenmoApp
{

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final UserOutput userOutput = new UserOutput();
    private final UserService userService = new UserService();
    private final AccountService accountService = new AccountService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public void run()
    {
        while (true) {
            currentUser = null;
            userOutput.printGreeting();
            loginMenu();
            if (currentUser != null) {
                mainMenu();
            }
        }
    }

    private void loginMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null)
        {
            userOutput.printLoginMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                handleRegister();
            }
            else if (menuSelection == 2)
            {
                handleLogin();
            }
            else if (menuSelection != 0)
            {
                System.out.println("Invalid Selection");
                userOutput.pause();
            }
        }
    }

    private void handleRegister()
    {
        System.out.println("Please register a new user account");
        UserCredentials credentials = userOutput.promptForCredentials();
        if (authenticationService.register(credentials))
        {
            System.out.println("Registration successful. You can now login.");
        }
        else
        {
            userOutput.printErrorMessage();
        }
    }

    private void handleLogin()
    {
        UserCredentials credentials = userOutput.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null)
        {
            userOutput.printErrorMessage();
        }
    }


    private void mainMenu()
    {
        userOutput.printMainMenuGreeting();
        int menuSelection = -1;
        while (menuSelection != 0)
        {
            userOutput.printMainMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                viewCurrentBalance();
            }
            else if (menuSelection == 2)
            {
                //view approved transfers
                viewTransferHistory();
            }
            else if (menuSelection == 3)
            {
                sendBucks();
            }
            else if (menuSelection == 4)
            {
                requestBucks();
            }
            else if (menuSelection == 0)
            {
                continue;
            }
            else
            {
                System.out.println("Invalid Selection");
            }
            userOutput.pause();
        }
    }

    private void viewCurrentBalance()
    {
        accountService.setAuthToken(currentUser.getToken());
        System.out.println("Your current balance is: " + UserOutput.TEXT_GREEN + "$" + accountService.viewCurrentBalance(currentUser.getUser().getId()) + UserOutput.TEXT_RESET);
        // transfer method call
    }

    private void viewTransferHistory()
    {
        accountService.setAuthToken(currentUser.getToken());
        Transfer[] transferHistory = accountService.viewTransferHistory();
        userOutput.viewTransferHistory(transferHistory, currentUser);

        while (true) {
            int transferId = 0;

            // viewing pending transfers
                Transfer transfer = new Transfer();
                boolean transferIdExists = false;

                while (true) {
                    transferId = userOutput.promptForIntAndZeroToCancel("Please enter transfer ID to view details or to approve/deny if the transfer is pending");

                    for (Transfer currentTransfer : transferHistory) {
                        if (transferId == currentTransfer.getTransferId()) {
                            transfer = currentTransfer;
                            transferIdExists = true;
                        }
                    }

                    if (transferId == 0) {
                        break;
                    }

                    if (transfer.getTransferStatus() == 1) {

                        if (transferIdExists && transfer.getAccountFrom().equals(currentUser.getUser().getUsername())) {
                            userOutput.approveOrDenyPrompt();
                            int choice = userOutput.promptForInt("Please choose an option: ");

                            if (choice == 0) {
                                break;
                            }

                            if((transfer.getAmount().compareTo(accountService.viewCurrentBalance(currentUser.getUser().getId())) <= 0 &&
                                choice == 1) || choice == 2) {

                                String approveOrDenyStatus = accountService.approveOrDenyPendingTransfer(choice, transfer);
                                System.out.println(approveOrDenyStatus);
                                break;
                            }
                            else if (transfer.getAmount().compareTo(accountService.viewCurrentBalance(currentUser.getUser().getId())) > 0 && choice == 1){
                                System.out.println(UserOutput.TEXT_RED + "You don't have enough money in your account to approve this transaction." + UserOutput.TEXT_RESET);
                            }
                        }

                        else {
                            System.out.println(UserOutput.TEXT_RED + "You can't approve or deny requests that you have sent. Please try again." + UserOutput.TEXT_RESET);
                        }
                    }

                    //viewing approved transfers
                    if (transfer.getTransferStatus() == 2 || transfer.getTransferStatus() == 3) {
//                        transferId = userOutput.promptForIntAndZeroToCancel("Please enter transfer ID to view details");
                        userOutput.viewTransferDetails(transferId, transferHistory);
                        break;
                    }

                }


            // exit
            if (transferId == 0) {
                System.out.println("Returning to Main Menu.");
                break;
            }
            break;
        }
    }


    private void sendBucks()
    {
        // greeting screen
        userOutput.printSendBucksGreeting();

        // display user list without the user that's currently logged in
        userService.setAuthToken(currentUser.getToken());
        accountService.setAuthToken(currentUser.getToken());

        User[] users = userService.listAllOtherUsers();
        userOutput.listAllOtherUsers(users);
        System.out.println("\n----------------------\n");


        boolean isTransferSuccessful = false;
        // prompts for user id and amount to send
        while (true) {
            // prompt for user id
            int recipientId = userOutput.promptForIntAndZeroToCancel("ID of user to send money to");
            boolean recipientIdExists = false;
            int currentUserId = currentUser.getUser().getId();
            boolean isInputTheCurrentUser = recipientId == currentUserId;

            if(recipientId == 0) { break; }

            for (User user : users) {
                // if the input is found in the users list, continue logic
                if (user.getId() == recipientId) {
                    recipientIdExists = true;
                    break;
                }
            }
            // is user the person currently logged in?
            if (isInputTheCurrentUser) {
                System.out.println("You can't send money to yourself ;) Please try again.");
                continue;
            }
            // is user id valid?
            if (!recipientIdExists) {
                System.out.println("The ID entered is not valid. Please try again.");
                continue;
            }

            BigDecimal amountToTransfer = userOutput.promptForBigDecimal("Amount to send: $");

            boolean isAmountLessThanZero = amountToTransfer.compareTo(BigDecimal.ZERO) <= 0;
            boolean isAmountGreaterThanCurrentBalance = accountService.viewCurrentBalance(currentUser.getUser().getId()).compareTo(amountToTransfer) < 0;

            if (isAmountLessThanZero) {
                System.out.println("The amount must be greater than zero. Please try again.");
                continue;
            }
            else if (isAmountGreaterThanCurrentBalance) {
                System.out.println("You don't have enough TE bucks to transfer this amount. Please try again.\n");
                continue;
            }
//                accountService.transferBucks(currentUser.getUser().getId(), recipientId, amountToTransfer);

                Transfer transaction = new Transfer(currentUserId, recipientId, amountToTransfer);

                isTransferSuccessful = accountService.transferBucks(transaction);
                break;
        }

        if (isTransferSuccessful) {
            System.out.println("Your transfer was successful!");
        }

    }

    private void requestBucks()
    {
        userOutput.printRequestBucksGreeting();
        // display possible users to request money from
        userService.setAuthToken(currentUser.getToken());
        accountService.setAuthToken(currentUser.getToken());
        System.out.println();
        User[] users = userService.listAllOtherUsers();
        userOutput.listAllOtherUsers(users);
        System.out.println("\n----------------------\n");

        int currentUserId = currentUser.getUser().getId();

        boolean requestMoneySuccessful = false;
        while (true) {
            int requestMoneyFromId = userOutput.promptForIntAndZeroToCancel("Please enter ID of user you are requesting from");

            if (requestMoneyFromId == 0) { break; }

            boolean requestMoneyFromIdExists = false;
            boolean isInputTheCurrentUser = requestMoneyFromId == currentUserId;

            for (User user : users) {
                // if the input is found in the users list, continue logic
                if (user.getId() == requestMoneyFromId) {
                    requestMoneyFromIdExists = true;
                    break;
                }
            }

            // is user the person currently logged in?
            if (isInputTheCurrentUser) {
                System.out.println("You can't request money to yourself ;) Please try again.");
                continue;
            }
            // is user id valid?
            if (!requestMoneyFromIdExists) {
                System.out.println("The ID entered is not valid. Please try again.");
                continue;
            }

            BigDecimal requestAmount = userOutput.promptForBigDecimal("Please enter amount to request: ");

            boolean isAmountLessThanZero = requestAmount.compareTo(BigDecimal.ZERO) <= 0;

            if (isAmountLessThanZero) {
                System.out.println("The amount must be greater than zero. Please try again.");
                continue;
            }

            Transfer requestMoney = new Transfer(currentUserId, requestMoneyFromId, requestAmount);
            requestMoneySuccessful = accountService.requestMoney(requestMoney);

            if (requestMoneySuccessful) {
                System.out.println("Request sent!");
            } else {
                System.out.println("An error has occurred.");
            }

            break;
        }
    }

}
