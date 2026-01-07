import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private final Airline airline = new Airline();
    private final JTextArea output;

    private final Color SIDEBAR_BG = new Color(34, 45, 50);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color HOVER_COLOR = new Color(60, 141, 188);

    public Main() {
        setTitle("✈ SkyLink Management System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initial Data Load
        FileManager.loadFlights(airline);

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 600));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JLabel menuTitle = new JLabel("MAIN MENU");
        menuTitle.setForeground(new Color(184, 199, 206));
        menuTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(menuTitle);

        sidebar.add(createSidebarButton("Add New Flight", e -> addFlightAction()));
        sidebar.add(createSidebarButton("Register Passenger", e -> addPassengerAction()));
        sidebar.add(createSidebarButton("View Schedule", e -> showFlightsAction()));
        sidebar.add(createSidebarButton("Search Flights", e -> searchFlightAction()));
        sidebar.add(createSidebarButton("Sort Schedule", e -> sortFlightsAction()));
        sidebar.add(createSidebarButton("Save Changes", e -> saveAction()));

        add(sidebar, BorderLayout.WEST);

        // --- CONTENT AREA ---
        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(new Color(244, 246, 249));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Flight Operations Dashboard");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        content.add(header, BorderLayout.NORTH);

        output = new JTextArea();
        output.setFont(new Font("Consolas", Font.PLAIN, 14));
        output.setEditable(false);
        output.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(new LineBorder(new Color(210, 214, 222), 1));
        content.add(scroll, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // Show welcome message
        output.setText("Welcome back! Loaded " + airline.flightCount + " flights from database.");
    }

    private JButton createSidebarButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(210, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(HOVER_COLOR); }
            public void mouseExited(MouseEvent e) { btn.setBackground(SIDEBAR_BG); }
        });
        btn.addActionListener(action);
        return btn;
    }

    private String getValidInput(String message) {
        String input = JOptionPane.showInputDialog(this, message);
        return (input == null || input.trim().isEmpty()) ? null : input.trim();
    }

    private Integer getValidInt(String message) {
        String input = JOptionPane.showInputDialog(this, message);
        if (input == null) return null;
        try { return Integer.parseInt(input.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private void addFlightAction() {
        String fno = getValidInput("Flight Number:");
        String dest = getValidInput("Destination:");
        if (fno != null && dest != null) {
            airline.addFlight(fno, dest);
            output.setText("✅ Added Flight: " + fno + "\nPress 'View Schedule' to refresh list.");
        }
    }

    private void addPassengerAction() {
        String fn = getValidInput("Enter Flight Number:");
        Flight f = airline.findFlight(fn);
        if (f == null) { output.setText("❌ Flight " + fn + " not found."); return; }

        String name = getValidInput("Passenger Name:");
        Integer age = getValidInt("Passenger Age:");
        String pass = getValidInput("Passport ID:");

        if (name != null && age != null && pass != null) {
            f.addPassenger(name, age, pass);
            output.setText("✅ " + name + " added to " + fn);
        }
    }

    private void showFlightsAction() {
        output.setText(String.format("%-15s | %-20s | %-10s\n", "FLIGHT NO", "DESTINATION", "PASSENGERS"));
        output.append("------------------------------------------------------------\n");
        Flight temp = airline.head;
        if (temp == null) { output.append("No flights in system."); return; }
        while (temp != null) {
            output.append(String.format("%-15s | %-20s | %-10d\n", temp.flightNo, temp.destination, temp.getPassengerCount()));
            temp = temp.next;
        }
    }

    private void searchFlightAction() {
        String key = getValidInput("Flight Number to search:");
        Flight f = airline.findFlight(key);
        if (f != null) {
            output.setText("🔍 FLIGHT DETAILS FOUND:\n");
            output.append("No: " + f.flightNo + "\nDest: " + f.destination + "\n\nPassengers:\n" + f.getPassengerList());
        } else { output.setText("❌ Flight not found."); }
    }

    private void sortFlightsAction() {
        airline.sortFlightsByNumber();
        showFlightsAction();
        output.append("\n✨ Schedule sorted alphabetically.");
    }

    private void saveAction() {
        try {
            FileManager.saveFlights(airline);
            JOptionPane.showMessageDialog(this, "💾 Data exported to flights.txt");
        } catch (Exception ex) {
            output.setText("❌ Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}