import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {

    private static final String FILE_NAME = "transaction_history.json";
    private Gson gson = new Gson();

    public void addTransaction(String username, String operation, double amount) {
        List<TransactionRecord> history = loadHistory();

        TransactionRecord record = new TransactionRecord();
        record.username = username;
        record.operation = operation;
        record.amount = amount;
        record.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        history.add(record);
        saveHistory(history);
    }

    public void showHistory(String username) {
        List<TransactionRecord> history = loadHistory();
        System.out.println("\n===== YOUR TRANSACTION HISTORY =====");

        boolean found = false;
        for (TransactionRecord record : history) {
            if (record.username.equals(username)) {
                System.out.println(record.timestamp + " | " + record.operation + " | €" + String.format("%.2f", record.amount));
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found.");
        }
        System.out.println("=====================================\n");
    }

    public void showAllHistory() {
        List<TransactionRecord> history = loadHistory();
        System.out.println("\n========== ALL TRANSACTION HISTORY ==========");

        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (TransactionRecord record : history) {
                System.out.println(record.username + " | " + record.timestamp + " | " +
                        record.operation + " | €" + String.format("%.2f", record.amount));
            }
        }
        System.out.println("=============================================\n");
    }

    private List<TransactionRecord> loadHistory() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type type = new TypeToken<List<TransactionRecord>>(){}.getType();
            List<TransactionRecord> history = gson.fromJson(reader, type);
            return history != null ? history : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void saveHistory(List<TransactionRecord> history) {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(history, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TransactionRecord {
        String username;
        String operation;
        double amount;
        String timestamp;
    }
}
