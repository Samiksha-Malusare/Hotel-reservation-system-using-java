import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {

    // Database credentials
    static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    static final String USER = "root";
    static final String PASS = "root";   // change if needed

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. Add Reservation");
            System.out.println("2. View Reservations");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addReservation();
                    break;
                case 2:
                    viewReservations();
                    break;
                case 3:
                    cancelReservation();
                    break;
                case 4:
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 4);
    }

    // Add reservation
    static void addReservation() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Guest Name: ");
            sc.nextLine();
            String name = sc.nextLine();

            System.out.print("Enter Room No: ");
            int room = sc.nextInt();

            System.out.print("Enter Contact No: ");
            String contact = sc.next();

            System.out.print("Enter Reservation Date (YYYY-MM-DD): ");
            String date = sc.next();

            String query = "INSERT INTO Reservation (Guest_name, Room_no, Contact_no, Reservation_date) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setInt(2, room);
            ps.setString(3, contact);
            ps.setDate(4, Date.valueOf(date));

            ps.executeUpdate();
            System.out.println("Reservation added successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View reservations
    static void viewReservations() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            String query = "SELECT * FROM Reservation";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\nID | Guest Name | Room | Contact | Date");
            System.out.println("--------------------------------------------");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("Reservation_id") + " | " +
                        rs.getString("Guest_name") + " | " +
                        rs.getInt("Room_no") + " | " +
                        rs.getString("Contact_no") + " | " +
                        rs.getDate("Reservation_date")
                );
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Cancel reservation
    static void cancelReservation() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Reservation ID to cancel: ");
            int id = sc.nextInt();

            String query = "DELETE FROM Reservation WHERE Reservation_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Reservation cancelled successfully!");
            else
                System.out.println("Reservation ID not found.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
