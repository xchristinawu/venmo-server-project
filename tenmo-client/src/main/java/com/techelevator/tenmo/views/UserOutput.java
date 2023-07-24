package com.techelevator.tenmo.views;


import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserOutput
{

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();

    // colors
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_BLUE = "\u001b[34;1m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_RESET = "\u001B[0m";

    public int promptForMenuSelection(String prompt)
    {
        int menuSelection;
        System.out.print(prompt);
        try
        {
            menuSelection = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e)
        {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting()
    {
        System.out.println(TEXT_CYAN + "  _____     ___     _  _    __  __     ___   \n" +
                " |_   _|   | __|   | \\| |  |  \\/  |   / _ \\  \n" +
                "   | |     | _|    | .` |  | |\\/| |  | (_) | \n" +
                "  _|_|_    |___|   |_|\\_|  |_|__|_|   \\___/  \n" +
                "_|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| \n" +
                "\"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' " + TEXT_RESET);
    }

    public void printMainMenuGreeting()
    {
        System.out.println(TEXT_CYAN + "__      __   ___      _        ___      ___    __  __     ___   \n" +
                "\\ \\    / /  | __|    | |      / __|    / _ \\  |  \\/  |   | __|  \n" +
                " \\ \\/\\/ /   | _|     | |__   | (__    | (_) | | |\\/| |   | _|   \n" +
                "  \\_/\\_/    |___|    |____|   \\___|    \\___/  |_|__|_|   |___|  \n" +
                "_|\"\"\"\"\"|  _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| \n" +
                "\"`-0-0-'  \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-'" + TEXT_RESET);
    }

    public void printSendBucksGreeting()
    {
        System.out.println(TEXT_CYAN + "   ___      ___     _  _      ___              __  __     ___     _  _      ___    __   __ \n" +
                "  / __|    | __|   | \\| |    |   \\      o O O |  \\/  |   / _ \\   | \\| |    | __|   \\ \\ / / \n" +
                "  \\__ \\    | _|    | .` |    | |) |    o      | |\\/| |  | (_) |  | .` |    | _|     \\ V /  \n" +
                "  |___/    |___|   |_|\\_|    |___/    TS__[O] |_|__|_|   \\___/   |_|\\_|    |___|    _|_|_  \n" +
                "_|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"|  {======| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _| \"\"\" | \n" +
                "\"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' ./o--000' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' " + TEXT_RESET);
    }

    public void printRequestBucksGreeting()
    {
        System.out.println(TEXT_CYAN + "   ___      ___     ___      _   _     ___      ___     _____             __  __     ___     _  _      ___    __   __ \n" +
                "  | _ \\    | __|   / _ \\    | | | |   | __|    / __|   |_   _|     o O O |  \\/  |   / _ \\   | \\| |    | __|   \\ \\ / / \n" +
                "  |   /    | _|   | (_) |   | |_| |   | _|     \\__ \\     | |      o      | |\\/| |  | (_) |  | .` |    | _|     \\ V /  \n" +
                "  |_|_\\    |___|   \\__\\_\\    \\___/    |___|    |___/    _|_|_    TS__[O] |_|__|_|   \\___/   |_|\\_|    |___|    _|_|_  \n" +
                "_|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"|  {======| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _| \"\"\" | \n" +
                "\"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' ./o--000' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' " + TEXT_RESET);
    }

    public void printTransfersGreeting()
    {
//        System.out.println("*********************");
//        System.out.println("*     Transfers     *");
//        System.out.println("*********************");

        System.out.println(TEXT_CYAN + "  _____     ___      ___     _  _      ___       ___     ___      ___      ___   \n" +
                " |_   _|   | _ \\    /   \\   | \\| |    / __|     | __|   | __|    | _ \\    / __|  \n" +
                "   | |     |   /    | - |   | .` |    \\__ \\     | _|    | _|     |   /    \\__ \\  \n" +
                "  _|_|_    |_|_\\    |_|_|   |_|\\_|    |___/    _|_|_    |___|    |_|_\\    |___/  \n" +
                "_|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| _| \"\"\" | _|\"\"\"\"\"| _|\"\"\"\"\"| _|\"\"\"\"\"| \n" +
                "\"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-' \"`-0-0-'" + TEXT_RESET);
    }

    public void printLoginMenu()
    {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu()
    {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your pending requests and transfer history");
        System.out.println("3: Send TE bucks");
        System.out.println("4: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials()
    {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt)
    {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForIntAndZeroToCancel(String message) {
        return promptForInt(message + " (0 to cancel): ");
    }

    public int promptForInt(String prompt)
    {
        System.out.print(prompt);
        while (true)
        {
            try
            {
                return Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt)
    {
        System.out.print(prompt);
        while (true)
        {
            try
            {
                return new BigDecimal(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause()
    {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage()
    {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void listAllOtherUsers(User[] users) {
        //User[] users = userService.listAllOtherUsers();
        for (User item : users) {
            System.out.println("ID: " + item.getId() + ", Username: " + item.getUsername());
        }
    }

    public void viewTransferHistory(Transfer[] transfers, AuthenticatedUser currentUser) {

        printTransfersGreeting();
        System.out.println("Status Key:" + TEXT_YELLOW + " ⌛ = Pending" + TEXT_GREEN + " ✅ = Approved" + TEXT_RED + " ❌ = Denied\n" + TEXT_RESET);
        System.out.printf("%-15s %-30s %-20s %-20s %-15s\n", "ID", "From/To", "Amount", "Transfer Type", "Status");

        for (Transfer transfer : transfers) {

            String personFromOrTo = "";
            if (transfer.getAccountFrom().equals(currentUser.getUser().getUsername()) || transfer.getAccountTo().equals(currentUser.getUser().getUsername())) {
                personFromOrTo += "To:" + transfer.getAccountTo() + " | From: " + transfer.getAccountFrom();
            }

            String transferType = "";
            transferType = transfer.getTransferType() == 1 ? "Request" : "Send";

            String transferStatus = "";
            String color = TEXT_RESET;
            if(transfer.getTransferStatus() == 1) {
                transferStatus = "⌛";
                color = TEXT_YELLOW;
            }
            else if (transfer.getTransferStatus() == 2) {
                transferStatus = "✅";
                color = TEXT_GREEN;
            }
            else {
                transferStatus = "❌";
                color = TEXT_RED;
            }

            System.out.printf("%-20s %-30s %-20s %-20s %-20s\n", color + transfer.getTransferId(), personFromOrTo, "$" + transfer.getAmount()
                                , transferType, transferStatus + TEXT_RESET);

        }

    }

    public void viewTransferDetails(int transferId, Transfer[] transferHistory) {

        boolean transferIdExists = false;
        for (Transfer transfer : transferHistory) {
            if (transfer.getTransferId() == transferId) {
                System.out.println("----------");
                System.out.println("Transfer Id: " + transfer.getTransferId());
                System.out.println("From: " + transfer.getAccountFrom());
                System.out.println("To: " + transfer.getAccountTo());
                System.out.println("Type: " + (transfer.getTransferType() == 2 ? "Send" : "Request"));

                String transferStatus = "";
                if (transfer.getTransferStatus() == 1) {
                    transferStatus += "Pending";
                } else if (transfer.getTransferStatus() == 2) {
                    transferStatus += "Approved";
                } else {
                    transferStatus += "Rejected";
                }
                System.out.println("Status: " + transferStatus);

                System.out.println("Amount: $" + transfer.getAmount());
                System.out.println("----------");
                System.out.println();
                transferIdExists = true;
            }
        }

        if (!transferIdExists && transferId != 0) {
            System.out.println("Invalid Transfer Id.");
        }
    }

    public void approveOrDenyPrompt() {
        System.out.println("1: Approve");
        System.out.println("2: Deny");
        System.out.println("0: Don't approve or deny");
        System.out.println("----------");
    }



}
