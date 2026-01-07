import java.io.*;

public class FileManager {

    public static void saveFlights(Airline airline) throws Exception {
        // Use a standard encoding to ensure special characters in names appear correctly
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("flights.txt"))) {

            // Header for the file
            bw.write(String.format("%-15s | %-25s | %-10s", "FLIGHT NO", "DESTINATION", "PASSENGERS"));
            bw.newLine();
            bw.write("----------------------------------------------------------------------");
            bw.newLine();

            Flight temp = airline.head;
            while (temp != null) {
                int pCount = temp.getPassengerCount();
                // Format the Flight line
                bw.write(String.format("%-15s | %-25s | %-10d",
                        temp.flightNo, temp.destination, pCount));
                bw.newLine();

                // Save individual passenger details
                Passenger p = temp.pHead;
                if (p != null) {
                    bw.write("   [Passenger List]:");
                    bw.newLine();
                    while (p != null) {
                        // Increased name padding to %-25s so names are visible and aligned
                        bw.write(String.format("   - Name: %-25s | Age: %-3d | Passport: %-10s",
                                p.name, p.age, p.passport));
                        bw.newLine();
                        p = p.next;
                    }
                }
                bw.write("----------------------------------------------------------------------");
                bw.newLine();
                temp = temp.next;
            }
        }
    }

    public static void loadFlights(Airline airline) {
        File file = new File("flights.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Flight currentFlight = null;

            // Skip header and separator
            br.readLine();
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("---") || line.trim().isEmpty() || line.contains("[Detailed")) continue;

                // Improved logic to detect and parse passenger lines
                if (line.trim().startsWith("- Name:")) {
                    if (currentFlight != null) {
                        // Extracting Name
                        String name = line.substring(line.indexOf("Name:") + 5, line.indexOf("|")).trim();
                        // Extracting Age
                        String agePart = line.substring(line.indexOf("Age:") + 4, line.lastIndexOf("|")).trim();
                        int age = Integer.parseInt(agePart);
                        // Extracting Passport
                        String passport = line.substring(line.indexOf("Passport:") + 9).trim();

                        currentFlight.addPassenger(name, age, passport);
                    }
                }
                // Logic to detect flight lines
                else if (line.contains("|")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 2) {
                        String fno = parts[0].trim();
                        String dest = parts[1].trim();
                        airline.addFlight(fno, dest);
                        currentFlight = airline.tail;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}