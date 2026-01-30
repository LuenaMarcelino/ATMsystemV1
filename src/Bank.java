import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Bank {
    private Map<String, UserData> users;
    private Gson gson = new Gson();
    private String dataFile = "users.json";
    private ATMStatus atmStatus;
    private TransactionHistory transactionHistory;

    public Bank(ATMStatus atmStatus) {
        this.atmStatus = atmStatus;
        this.transactionHistory = new TransactionHistory();
        loadUsers();
    }

    private void loadUsers() {
        try (Reader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, UserData>>() {}.getType();
            users = gson.fromJson(reader, type);
            if (users == null) users = new HashMap<>();
        } catch (IOException e) {
            users = new HashMap<>();
        }
    }

    private void saveUsers() {
        try (Writer writer = new FileWriter(dataFile)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String username, String password) {
        if (users.containsKey(username)) {
            return users.get(username).getPassword().equals(password);
        }
        return false;
    }

    public double checkBalance(String username) {
        return users.get(username).getBalance();
    }

    public boolean withdraw(String username, double amount) {
        UserData user = users.get(username);

        // Check if user has enough balance
        if (amount <= 0 || user.getBalance() < amount) {
            System.out.println("❌ Insufficient balance.");
            return false;
        }

        // Check if ATM has enough cash
        if (!atmStatus.canDispense(amount)) {
            System.out.println("ATM cannot dispense this amount. Either out of cash or cannot make exact change.");
            return false;
        }

        // Dispense cash
        if (atmStatus.dispenseCash(amount)) {
            user.setBalance(user.getBalance() - amount);
            saveUsers();
            transactionHistory.addTransaction(username, "Withdrawal", amount);
            return true;
        }

        return false;
    }

    public void deposit(String username, double amount) {
        UserData user = users.get(username);
        user.setBalance(user.getBalance() + amount);
        saveUsers();
        transactionHistory.addTransaction(username, "Deposit", amount);
    }

    public boolean transfer(String fromUser, String toUser, double amount) {
        if (!users.containsKey(toUser)) {
            System.out.println("Recipient not found.");
            return false;
        }

        UserData sender = users.get(fromUser);

        if (amount <= 0 || sender.getBalance() < amount) {
            System.out.println("Insufficient balance.");
            return false;
        }

        // Perform transfer
        sender.setBalance(sender.getBalance() - amount);
        users.get(toUser).setBalance(users.get(toUser).getBalance() + amount);
        saveUsers();
        transactionHistory.addTransaction(fromUser, "Transfer to " + toUser, amount);
        return true;
    }

    public int getNumberOfUsers() {
        return users.size();
    }

    public double getTotalMoney() {
        return users.values().stream().mapToDouble(UserData::getBalance).sum();
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
}