import com.google.gson.Gson;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ATMStatus {

    // Banknotes
    private int banknotes10;
    private int banknotes20;
    private int banknotes50;
    private int banknotes100;

    // Consumables
    private int inkLevel;
    private int paperLevel;

    // Versions
    private String softwareVersion;
    private String hardwareVersion;

    private static final String FILE_NAME = "atm_status.json";
    private Gson gson = new Gson();

    public ATMStatus() {
        load();
    }

    private void load() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Map<String, Object> data = gson.fromJson(reader, Map.class);
            this.banknotes10 = ((Number) data.get("banknotes10")).intValue();
            this.banknotes20 = ((Number) data.get("banknotes20")).intValue();
            this.banknotes50 = ((Number) data.get("banknotes50")).intValue();
            this.banknotes100 = ((Number) data.get("banknotes100")).intValue();
            this.inkLevel = ((Number) data.get("inkLevel")).intValue();
            this.paperLevel = ((Number) data.get("paperLevel")).intValue();
            this.softwareVersion = (String) data.get("softwareVersion");
            this.hardwareVersion = (String) data.get("hardwareVersion");
        } catch (Exception e) {
            // Default LOW values for testing (as teacher requested)
            banknotes10 = 5;
            banknotes20 = 5;
            banknotes50 = 3;
            banknotes100 = 2;
            inkLevel = 10;      // Only 10 receipts worth
            paperLevel = 10;    // Only 10 receipts
            softwareVersion = "1.0";
            hardwareVersion = "1.0";
            save();
        }
    }

    private void save() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            Map<String, Object> data = new HashMap<>();
            data.put("banknotes10", banknotes10);
            data.put("banknotes20", banknotes20);
            data.put("banknotes50", banknotes50);
            data.put("banknotes100", banknotes100);
            data.put("inkLevel", inkLevel);
            data.put("paperLevel", paperLevel);
            data.put("softwareVersion", softwareVersion);
            data.put("hardwareVersion", hardwareVersion);
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStatus() {
        System.out.println("\n========== ATM STATUS ==========");
        System.out.println("BANKNOTES:");
        System.out.println("  €10 notes:  " + banknotes10 + " (€" + (banknotes10 * 10) + ")");
        System.out.println("  €20 notes:  " + banknotes20 + " (€" + (banknotes20 * 20) + ")");
        System.out.println("  €50 notes:  " + banknotes50 + " (€" + (banknotes50 * 50) + ")");
        System.out.println("  €100 notes: " + banknotes100 + " (€" + (banknotes100 * 100) + ")");
        System.out.println("  TOTAL CASH: €" + getTotalCash());
        System.out.println("\nCONSUMABLES:");
        System.out.println("  Ink level:   " + inkLevel + " receipts");
        System.out.println("  Paper level: " + paperLevel + " receipts");
        System.out.println("\nVERSIONS:");
        System.out.println("  Software: " + softwareVersion);
        System.out.println("  Hardware: " + hardwareVersion);
        System.out.println("================================\n");
    }

    public double getTotalCash() {
        return (banknotes10 * 10) + (banknotes20 * 20) +
                (banknotes50 * 50) + (banknotes100 * 100);
    }

    public boolean canDispense(double amount) {
        if (amount > getTotalCash()) {
            return false;
        }

        // Check if we can make exact change
        int[] banknotes = {100, 50, 20, 10};
        int[] available = {banknotes100, banknotes50, banknotes20, banknotes10};

        return canMakeAmount(amount, banknotes, available, 0);
    }

    private boolean canMakeAmount(double remaining, int[] banknotes, int[] available, int index) {
        if (remaining < 0.01) return true;
        if (index >= banknotes.length) return false;

        int maxUsable = Math.min((int)(remaining / banknotes[index]), available[index]);

        for (int i = maxUsable; i >= 0; i--) {
            if (canMakeAmount(remaining - (i * banknotes[index]), banknotes, available, index + 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispenseCash(double amount) {
        // Greedy algorithm: use largest banknotes first
        int needed100 = 0, needed50 = 0, needed20 = 0, needed10 = 0;
        double remaining = amount;

        needed100 = Math.min((int)(remaining / 100), banknotes100);
        remaining -= needed100 * 100;

        needed50 = Math.min((int)(remaining / 50), banknotes50);
        remaining -= needed50 * 50;

        needed20 = Math.min((int)(remaining / 20), banknotes20);
        remaining -= needed20 * 20;

        needed10 = Math.min((int)(remaining / 10), banknotes10);
        remaining -= needed10 * 10;

        if (remaining > 0.01) {
            return false;
        }

        // Deduct banknotes
        banknotes100 -= needed100;
        banknotes50 -= needed50;
        banknotes20 -= needed20;
        banknotes10 -= needed10;
        save();

        System.out.println("\n💶 Dispensing: " + needed100 + "x€100, " + needed50 + "x€50, "
                + needed20 + "x€20, " + needed10 + "x€10");
        return true;
    }

    public boolean hasInkAndPaper() {
        return inkLevel > 0 && paperLevel > 0;
    }

    public void useInkAndPaper() {
        if (inkLevel > 0) inkLevel--;
        if (paperLevel > 0) paperLevel--;
        save();
    }

    public int getInkLevel() {
        return inkLevel;
    }

    public int getPaperLevel() {
        return paperLevel;
    }

    public void refillBanknotes(int note, int quantity) {
        switch(note) {
            case 10 -> banknotes10 += quantity;
            case 20 -> banknotes20 += quantity;
            case 50 -> banknotes50 += quantity;
            case 100 -> banknotes100 += quantity;
            default -> System.out.println("Invalid banknote denomination");
        }
        save();
        System.out.println("Refilled " + quantity + " x €" + note + " notes");
    }

    public void refillInk(int amount) {
        inkLevel += amount;
        save();
        System.out.println("Ink refilled. New level: " + inkLevel + " receipts");
    }

    public void refillPaper(int amount) {
        paperLevel += amount;
        save();
        System.out.println("Paper refilled. New level: " + paperLevel + " receipts");
    }

    public void upgradeSoftware() {
        softwareVersion = softwareVersion + ".1";
        save();
        System.out.println("Software upgraded to version " + softwareVersion);
    }

    public void upgradeHardware() {
        hardwareVersion = hardwareVersion + ".1";
        save();
        System.out.println("Hardware upgraded to version " + hardwareVersion);
    }
}
