import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ATMStatus atmStatus = new ATMStatus();
        Bank bank = new Bank(atmStatus);

        System.out.println("╔════════════════════════════╗");
        System.out.println("║    Welcome to the ATM!     ║");
        System.out.println("╚════════════════════════════╝\n");

        boolean systemRunning = true;

        while (systemRunning) {
            System.out.println("\n--- LOGIN MENU ---");
            System.out.println("1. Customer Login");
            System.out.println("2. Technician Login");
            System.out.println("3. Exit System");
            System.out.print("Choose option: ");

            int loginChoice;
            try {
                loginChoice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
                continue;
            }

            switch (loginChoice) {
                case 1 -> customerLogin(sc, bank, atmStatus);
                case 2 -> technicianLogin(sc, atmStatus, bank);
                case 3 -> {
                    System.out.println("\nThank you for using our ATM. Goodbye!");
                    systemRunning = false;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void customerLogin(Scanner sc, Bank bank, ATMStatus atmStatus) {
        System.out.print("\nEnter username: ");
        String username = sc.nextLine().trim();

        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();

        if (!bank.authenticate(username, password)) {
            System.out.println("❌ Authentication failed");
            return;
        }

        System.out.println("Customer login successful!");
        customerMenu(sc, bank, atmStatus, username);
    }

    private static void customerMenu(Scanner sc, Bank bank, ATMStatus atmStatus, String username) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n╔════════════════════════════╗");
            System.out.println("║      CUSTOMER MENU         ║");
            System.out.println("╚════════════════════════════╝");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Funds");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    double balance = bank.checkBalance(username);
                    System.out.println("\n💰 Current Balance: €" + String.format("%.2f", balance));
                    askForReceipt(sc, username, "Balance Check", 0, balance, atmStatus);
                }
                case 2 -> {
                    System.out.print("Enter amount to withdraw: €");
                    double amount = Double.parseDouble(sc.nextLine());

                    if (bank.withdraw(username, amount)) {
                        System.out.println("Withdrawal successful");
                        double newBalance = bank.checkBalance(username);
                        askForReceipt(sc, username, "Withdrawal", amount, newBalance, atmStatus);
                    }
                }
                case 3 -> {
                    System.out.print("Enter amount to deposit: €");
                    double amount = Double.parseDouble(sc.nextLine());

                    if (amount <= 0) {
                        System.out.println("❌ Invalid amount");
                    } else {
                        bank.deposit(username, amount);
                        System.out.println("Deposit successful");
                        double newBalance = bank.checkBalance(username);
                        askForReceipt(sc, username, "Deposit", amount, newBalance, atmStatus);
                    }
                }
                case 4 -> {
                    System.out.print("Enter recipient username: ");
                    String toUser = sc.nextLine().trim();
                    System.out.print("Enter amount to transfer: €");
                    double amount = Double.parseDouble(sc.nextLine());

                    if (bank.transfer(username, toUser, amount)) {
                        System.out.println("Transfer successful");
                        double newBalance = bank.checkBalance(username);
                        askForReceipt(sc, username, "Transfer to " + toUser, amount, newBalance, atmStatus);
                    }
                }
                case 5 -> bank.getTransactionHistory().showHistory(username);
                case 6 -> {
                    logout = true;
                    System.out.println("\nLogged out successfully");
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void askForReceipt(Scanner sc, String username, String operation,
                                      double amount, double balance, ATMStatus atmStatus) {
        System.out.print("\nDo you want a receipt? (yes/no): ");
        String response = sc.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            Receipt.print(username, operation, amount, balance, atmStatus);
        } else {
            System.out.println("No receipt printed.");
        }
    }

    private static void technicianLogin(Scanner sc, ATMStatus atmStatus, Bank bank) {
        System.out.print("\nEnter technician username: ");
        String username = sc.nextLine().trim();

        System.out.print("Enter technician password: ");
        String password = sc.nextLine().trim();

        if (!username.equals("admin") || !password.equals("admin123")) {
            System.out.println("❌ Authentication failed");
            return;
        }

        System.out.println("Technician login successful!");
        technicianMenu(sc, atmStatus, bank);
    }

    private static void technicianMenu(Scanner sc, ATMStatus atmStatus, Bank bank) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n╔════════════════════════════╗");
            System.out.println("║     TECHNICIAN MENU        ║");
            System.out.println("╚════════════════════════════╝");
            System.out.println("1. View ATM Status");
            System.out.println("2. View All Transaction History");
            System.out.println("3. Refill Banknotes");
            System.out.println("4. Refill Ink");
            System.out.println("5. Refill Paper");
            System.out.println("6. Upgrade Software");
            System.out.println("7. Upgrade Hardware");
            System.out.println("8. Logout");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
                continue;
            }

            switch (choice) {
                case 1 -> atmStatus.showStatus();
                case 2 -> bank.getTransactionHistory().showAllHistory();
                case 3 -> {
                    System.out.println("\nSelect banknote to refill:");
                    System.out.println("1. €10");
                    System.out.println("2. €20");
                    System.out.println("3. €50");
                    System.out.println("4. €100");
                    System.out.print("Choice: ");
                    int noteChoice = Integer.parseInt(sc.nextLine());

                    int noteValue = switch(noteChoice) {
                        case 1 -> 10;
                        case 2 -> 20;
                        case 3 -> 50;
                        case 4 -> 100;
                        default -> 0;
                    };

                    if (noteValue > 0) {
                        System.out.print("Enter quantity to add: ");
                        int quantity = Integer.parseInt(sc.nextLine());
                        atmStatus.refillBanknotes(noteValue, quantity);
                    }
                }
                case 4 -> {
                    System.out.print("Enter ink amount to add (receipts): ");
                    int amount = Integer.parseInt(sc.nextLine());
                    atmStatus.refillInk(amount);
                }
                case 5 -> {
                    System.out.print("Enter paper amount to add (receipts): ");
                    int amount = Integer.parseInt(sc.nextLine());
                    atmStatus.refillPaper(amount);
                }
                case 6 -> atmStatus.upgradeSoftware();
                case 7 -> atmStatus.upgradeHardware();
                case 8 -> {
                    logout = true;
                    System.out.println("\nTechnician logged out");
                }
                default -> System.out.println("Invalid option");
            }
        }
    }
}