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
//            } else if (menuSelection == 3) {
//                viewPendingRequests();
            } else if (menuSelection == 3) {
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
        int transferChoice = -1;
        Account account = appService.getAccountById(currentUser.getUser().getId());
        List<Transfer> transfers = appService.getTransfersByAccountId(account.getAccountId());

        // Print transfers
        System.out.println(
                "\n-------------------------------------------\n" +
                "Transfers\n" +
                String.format("%-12s%-24s%7s\n", "ID", "From/To", "Amount") +
                "-------------------------------------------");

        for (Transfer transfer : transfers) {
            long tId = transfer.getTransferId();
            String tDirection = (transfer.getAccountFrom() == account.getAccountId()) ? "To:" : "From:";
            String tParty = (tDirection.equals("To:")) ? appService.getUserByAccountId(transfer.getAccountTo()).getUsername() : appService.getUserByAccountId(transfer.getAccountFrom()).getUsername();
            BigDecimal tMoney = transfer.getAmount();
            System.out.println(String.format("%-12s%-6s%-12s%13s", tId, tDirection, tParty, "$" + tMoney));
        }

        System.out.println("---------");

        // Transfer details option
        transferChoice = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        if (transferChoice > 0) {
            // Retrieve transfer details
            Transfer selectedTransfer = null;
            for (Transfer transfer : transfers) {
                if(transferChoice == transfer.getTransferId()) {
                    selectedTransfer = transfer;
                    break;
                }
            }
            if(selectedTransfer == null) {
                System.out.println("Invalid ID");
                return;
            }

            // Print transfer details
            System.out.println(
                    "\n--------------------------------------------\n" +
                    "Transfer Details\n" +
                    "--------------------------------------------\n" +
                    String.format("%-8s%-36s\n", "Id:", selectedTransfer.getTransferId()) +
                    String.format("%-8s%-36s\n", "From:", appService.getUserByAccountId(selectedTransfer.getAccountFrom()).getUsername()) +
                    String.format("%-8s%-36s\n", "To:", appService.getUserByAccountId(selectedTransfer.getAccountTo()).getUsername()) +
                    String.format("%-8s%-36s\n", "Type:", selectedTransfer.getTransferTypeId() == 1 ? "Request" : "Send") +
                    String.format("%-8s%-36s\n", "Status:",
                            selectedTransfer.getTransferStatusId() == 1 ? "Pending" :
                            selectedTransfer.getTransferStatusId() == 2 ? "Approved" : "Rejected") +
                    String.format("%-8s%-36s", "Amount:", "$" + selectedTransfer.getAmount()));
            System.out.println("---------");
        } else if (transferChoice == 0) {
            return;
        } else {
            System.out.println("Invalid selection");
        }
	}

//	private void viewPendingRequests() {
//		// TODO Auto-generated method stub
//
//	}

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

        // Check for availability of funds in currentUser's account
        if (currentAccount.getBalance().compareTo(transferAmount) < 0) {
            System.out.println("You don't have enough TE Bucks.");
            return;
        }

        // Set transfer details
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);
        transfer.setTransferStatusId(2);
        transfer.setAccountFrom(currentAccount.getAccountId());
        transfer.setAccountTo(targetAccount.getAccountId());
        transfer.setAmount(transferAmount);

        // Initiate transfer of transferAmount from currentUser's account to targetAccount.balance
        transfer = appService.createTransfer(transfer);

        // Update account object balances
        currentAccount.setBalance(currentAccount.getBalance().subtract(transferAmount));
        targetAccount.setBalance(targetAccount.getBalance().add(transferAmount));

        // Update DB account balances
        appService.updateAccount(currentAccount.getAccountId(), currentAccount);
        appService.updateAccount(targetAccount.getAccountId(), targetAccount);

        System.out.println("\n--------------------------------------------\n" +
                "Transfer Details\n" +
                "--------------------------------------------\n" +
                " Id: " + transfer.getTransferId() + "\n" +
                " From: " + currentUser.getUser().getUsername() + "\n" +
                " To: " + appService.getUserByAccountId(targetAccount.getAccountId()).getUsername() + "\n" +
                " Type: Send" + "\n" +
                " Status: Approved" + "\n" +
                " Amount: " + transfer.getAmount() +
                "---------\n"
        );
    }

//	private void requestBucks() {
//		// TODO Auto-generated method stub
//
//	}

}
