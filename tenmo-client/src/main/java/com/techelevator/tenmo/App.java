package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AppService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AppService appService = new AppService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
//            } else if (menuSelection == 5) {
//                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        System.out.println("Your current account balance is: $" +
                appService.getBalance(currentUser.getUser().getId()));
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub

		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

    private void sendBucks() {
        // TODO Auto-generated method stub

        Account currentAccount = appService.getAccountById(currentUser.getUser().getId());
        String searchTerm = consoleService.promptForString(
                "Please enter the username you'd like to send TEnmo Bucks to: ");
        System.out.println("-------------------------------------------\n" +
                "Account\n" +
                "ID          Name\n" +
                "-------------------------------------------");
        List<Account> accounts = appService.getAccountsByUsernameSearch(searchTerm);
        for (Account account : accounts) {
            System.out.println(account.getAccountId() + "     " +
                    (appService.getUserByAccountId(account.getAccountId()).getUsername()));
        }
        System.out.println("---------");
        long accountSelection = consoleService.promptForInt(
                "Enter ID of account you are sending to (0 to cancel):");
        Account targetAccount = null;
        for(Account account : accounts) {
            if(account.getAccountId() == accountSelection) {
                targetAccount = account;
                break;
            }
        }
        if(targetAccount == null) {
            System.out.println("No account was selected.");
            return;
        }
        if(targetAccount.getUserId() == currentUser.getUser().getId()) {
            System.out.println("Self-selection is not permitted.");
            return;
        }
        BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter amount:");
        if(transferAmount.compareTo(BigDecimal.valueOf(0.00)) <= 0) {
            System.out.println("Transfer canceled (amount must be greater than 0.00)");
            return;
        }

        //check for availability of funds in currentUser's account
        if (currentAccount.getBalance().compareTo(transferAmount) < 0) {
            System.out.println("You don't have enough TE Bucks.");
            return;
        }

        //initiate transfer of transferAmount from currentUser's account to targetAccount.balance
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);
        transfer.setTransferStatusId(2);
        transfer.setAccountFrom(currentAccount.getAccountId());
        transfer.setAccountTo(targetAccount.getAccountId());
        transfer.setAmount(transferAmount);
        transfer = appService.createTransfer(transfer);

        System.out.println(transfer.toString());

    }

//	private void requestBucks() {
//		// TODO Auto-generated method stub
//
//	}

}
