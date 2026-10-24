import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSystem {

    static int totalSlots;
    static String[] slotVehicleNumber;
    static String[] slotOwnerName;
    static String[] slotVehicleType;
    static String[] slotEntryTime;
    static boolean[] slotOccupied;
    static ArrayList<String> history = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  PARKING SLOT ALLOCATION & TRACKING SYS  ║");
        System.out.println("╚══════════════════════════════════════════╝");

        System.out.print("\nEnter total number of parking slots: ");
        totalSlots = scanner.nextInt();
        scanner.nextLine();

        slotVehicleNumber = new String[totalSlots];
        slotOwnerName     = new String[totalSlots];
        slotVehicleType   = new String[totalSlots];
        slotEntryTime     = new String[totalSlots];
        slotOccupied      = new boolean[totalSlots];

        System.out.println("\n  Parking lot ready with " + totalSlots + " slots!\n");

        while (true) {
            printMenu();

            System.out.print("Your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if      (choice == 1) parkVehicle();
            else if (choice == 2) removeVehicle();
            else if (choice == 3) viewStatus();
            else if (choice == 4) searchVehicle();
            else if (choice == 5) viewHistory();
            else if (choice == 6) {
                System.out.println("\n  Thank you! Goodbye.");
                break;
            }
            else {
                System.out.println("  Invalid choice! Enter a number from 1 to 6.");
            }
        }
    }

    static void printMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("  1. Park a Vehicle");
        System.out.println("  2. Remove a Vehicle");
        System.out.println("  3. View Parking Status");
        System.out.println("  4. Search for a Vehicle");
        System.out.println("  5. View Entry / Exit History");
        System.out.println("  6. Exit");
        System.out.println("================================");
    }

    static void parkVehicle() {
        System.out.println("\n--- Park a Vehicle ---");

        System.out.print("  Vehicle Number        : ");
        String vehicleNumber = scanner.nextLine().toUpperCase().trim();

        System.out.print("  Owner Name            : ");
        String ownerName = scanner.nextLine().trim();

        System.out.print("  Type (Car/Bike/Truck) : ");
        String vehicleType = scanner.nextLine().trim();

        for (int i = 0; i < totalSlots; i++) {
            if (slotOccupied[i] && slotVehicleNumber[i].equalsIgnoreCase(vehicleNumber)) {
                System.out.println("  This vehicle is already parked at Slot " + (i + 1) + "!");
                return;
            }
        }

        for (int i = 0; i < totalSlots; i++) {
            if (!slotOccupied[i]) {
                slotOccupied[i]      = true;
                slotVehicleNumber[i] = vehicleNumber;
                slotOwnerName[i]     = ownerName;
                slotVehicleType[i]   = vehicleType;
                slotEntryTime[i]     = getCurrentTime();

                String log = "ENTRY | Slot " + (i + 1) +
                             " | [" + vehicleType + "] " + vehicleNumber +
                             " | Owner: " + ownerName +
                             " | Time: " + slotEntryTime[i];
                history.add(log);

                System.out.println("  SUCCESS! Vehicle parked at Slot " + (i + 1));
                return;
            }
        }

        System.out.println("  FULL! No slots available right now.");
    }

    static void removeVehicle() {
        System.out.println("\n--- Remove a Vehicle ---");
        System.out.print("  Enter Vehicle Number to remove: ");
        String vehicleNumber = scanner.nextLine().toUpperCase().trim();

        for (int i = 0; i < totalSlots; i++) {
            if (slotOccupied[i] && slotVehicleNumber[i].equalsIgnoreCase(vehicleNumber)) {

                String log = "EXIT  | Slot " + (i + 1) +
                             " | [" + slotVehicleType[i] + "] " + vehicleNumber +
                             " | Owner: " + slotOwnerName[i] +
                             " | Exit Time: " + getCurrentTime();
                history.add(log);

                slotOccupied[i]      = false;
                slotVehicleNumber[i] = null;
                slotOwnerName[i]     = null;
                slotVehicleType[i]   = null;
                slotEntryTime[i]     = null;

                System.out.println("  SUCCESS! Vehicle " + vehicleNumber +
                                   " removed from Slot " + (i + 1));
                return;
            }
        }

        System.out.println("  Vehicle " + vehicleNumber + " not found in the parking lot.");
    }

    static void viewStatus() {
        int occupied  = 0;
        int available = 0;

        for (int i = 0; i < totalSlots; i++) {
            if (slotOccupied[i]) occupied++;
            else available++;
        }

        System.out.println("\n========== PARKING LOT STATUS ==========");
        System.out.println("  Total: " + totalSlots +
                           "  |  Occupied: " + occupied +
                           "  |  Available: " + available);
        System.out.println("-----------------------------------------");

        for (int i = 0; i < totalSlots; i++) {
            if (slotOccupied[i]) {
                System.out.println("  Slot " + (i + 1) + " [OCCUPIED]"
                    + "\n           Vehicle : " + slotVehicleNumber[i]
                    + "\n           Owner   : " + slotOwnerName[i]
                    + "\n           Type    : " + slotVehicleType[i]
                    + "\n           Entry   : " + slotEntryTime[i]);
            } else {
                System.out.println("  Slot " + (i + 1) + " [AVAILABLE]");
            }
        }

        System.out.println("=========================================");
    }

    static void searchVehicle() {
        System.out.println("\n--- Search Vehicle ---");
        System.out.print("  Enter Vehicle Number: ");
        String vehicleNumber = scanner.nextLine().toUpperCase().trim();

        for (int i = 0; i < totalSlots; i++) {
            if (slotOccupied[i] && slotVehicleNumber[i].equalsIgnoreCase(vehicleNumber)) {
                System.out.println("  FOUND at Slot " + (i + 1) + "!"
                    + "\n    Vehicle : " + slotVehicleNumber[i]
                    + "\n    Owner   : " + slotOwnerName[i]
                    + "\n    Type    : " + slotVehicleType[i]
                    + "\n    Entry   : " + slotEntryTime[i]);
                return;
            }
        }

        System.out.println("  Vehicle " + vehicleNumber + " is not currently parked here.");
    }

    static void viewHistory() {
        System.out.println("\n========== VEHICLE HISTORY LOG ==========");
        if (history.isEmpty()) {
            System.out.println("  No records yet.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + history.get(i));
            }
        }
        System.out.println("==========================================");
    }

    static String getCurrentTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.now().format(fmt);
    }
}
