import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Seeder di esempio per il database MySQL provaGAAAG.
 *
 * NOTE IMPORTANTI:
 * 1) Aggiorna URL, DB_USER e DB_PASSWORD.
 * 2) Controlla i valori ENUM nel tuo schema MySQL e, se necessario,
 *    modifica le costanti qui sotto.
 * 3) Il codice inserisce 5 record per ogni tabella rispettando le FK.
 */
public class DataSeeder {

    private static final String URL = "jdbc:mysql://localhost:3306/provaGAAAG?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    // Adatta questi valori agli ENUM reali del tuo schema, se diversi.
    private static final String[] USER_ROLES = {"USER", "ADMIN", "COMPANY", "USER", "USER"};
    private static final String[] ACTIVITY_STATUS = {"ACTIVE", "ACTIVE", "PENDING", "ACTIVE", "INACTIVE"};
    private static final String[] ACTIVITY_TYPES = {"SPORT", "WELLNESS", "FOOD", "TOUR", "EDUCATION"};
    private static final String[] DIARY_AVAILABLE = {"AVAILABLE", "AVAILABLE", "UNAVAILABLE", "AVAILABLE", "AVAILABLE"};
    private static final String[] BOOKING_STATUS = {"PENDING", "CONFIRMED", "CONFIRMED", "CANCELLED", "PENDING"};
    private static final String[] REVIEW_STATUS = {"VISIBLE", "VISIBLE", "PENDING", "VISIBLE", "HIDDEN"};
    private static final String[] REPORTING_STATUS = {"OPEN", "OPEN", "IN_PROGRESS", "CLOSED", "OPEN"};
    private static final String[] REPORTING_TYPES = {"USER", "BOOKING", "REVIEW", "SERVICE", "ACTIVITY"};

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL non trovato. Aggiungi mysql-connector-j al progetto.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            try {
                insertUsers(conn);
                insertActivities(conn);
                insertServices(conn);
                insertDiaries(conn);
                insertBookings(conn);
                insertReviews(conn);
                insertReportings(conn);

                conn.commit();
                System.out.println("Inserimento completato con successo.");
            } catch (Exception e) {
                conn.rollback();
                System.err.println("Errore durante il seeding. Rollback eseguito.");
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertUsers(Connection conn) throws Exception {
        String sql = """
                INSERT INTO user (dob, email, first_name, last_name, password, reporting_received, role)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                ps.setDate(1, Date.valueOf(LocalDate.of(1990 + i, i, Math.min(i + 5, 28))));
                ps.setString(2, "utente" + i + "@example.com");
                ps.setString(3, "Nome" + i);
                ps.setString(4, "Cognome" + i);
                ps.setString(5, "password" + i);
                ps.setInt(6, 0);
                ps.setString(7, USER_ROLES[i - 1]);
                ps.executeUpdate();
            }
        }

        System.out.println("Tabella user popolata.");
    }

    private static void insertActivities(Connection conn) throws Exception {
        String sql = """
                INSERT INTO activity (category, company_email, name, registration_date, status, type)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                ps.setString(1, "Categoria" + i);
                ps.setString(2, "azienda" + i + "@example.com");
                ps.setString(3, "Attivita " + i);
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().minusDays(10L - i)));
                ps.setString(5, ACTIVITY_STATUS[i - 1]);
                ps.setString(6, ACTIVITY_TYPES[i - 1]);
                ps.executeUpdate();
            }
        }

        System.out.println("Tabella activity popolata.");
    }

    private static void insertServices(Connection conn) throws Exception {
        String sql = """
                INSERT INTO service (description, duration_minutes, name, notes, price, activity_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                ps.setString(1, "Descrizione del servizio " + i);
                ps.setInt(2, 30 * i);
                ps.setString(3, "Servizio " + i);
                ps.setString(4, "Nota servizio " + i);
                ps.setInt(5, 20 * i);
                ps.setInt(6, i); // activity_id 1..5
                ps.executeUpdate();
            }
        }

        System.out.println("Tabella service popolata.");
    }

    private static void insertDiaries(Connection conn) throws Exception {
        String sql = """
                INSERT INTO diary (available, date, end_hour, start_hour, activity_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                ps.setString(1, DIARY_AVAILABLE[i - 1]);
                ps.setDate(2, Date.valueOf(LocalDate.now().plusDays(i)));
                ps.setTime(3, Time.valueOf(LocalTime.of(10 + i, 0)));
                ps.setTime(4, Time.valueOf(LocalTime.of(9 + i, 0)));
                ps.setInt(5, i); // activity_id 1..5
                ps.executeUpdate();
            }
        }

        System.out.println("Tabella diary popolata.");
    }

    private static void insertBookings(Connection conn) throws Exception {
        String sql = """
                INSERT INTO booking (booking_date, note, status, diary_id, service_id, user_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                ps.setDate(1, Date.valueOf(LocalDate.now().plusDays(i)));
                ps.setString(2, "Prenotazione di esempio " + i);
                ps.setString(3, BOOKING_STATUS[i - 1]);
                ps.setInt(4, i); // diary_id 1..5
                ps.setInt(5, i); // service_id 1..5
                ps.setInt(6, i); // user_id 1..5
                ps.executeUpdate();
            }
        }

        System.out.println("Tabella booking popolata.");
    }

    private static void insertReviews(Connection conn) throws Exception {
        String sql = """
                INSERT INTO review (content, rating, status, user_id, booking_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                ps.setString(1, "Recensione di esempio numero " + i);
                ps.setInt(2, Math.min(5, i));
                ps.setString(3, REVIEW_STATUS[i - 1]);
                ps.setInt(4, i); // user_id 1..5
                ps.setInt(5, i); // booking_id 1..5
                ps.executeUpdate();
            }
        }

        System.out.println("Tabella review popolata.");
    }

    private static void insertReportings(Connection conn) throws Exception {
        String sql = """
                INSERT INTO reporting (created_at, reason, status, type, author_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusHours(i * 3L)));
                ps.setString(2, "Segnalazione di esempio " + i);
                ps.setString(3, REPORTING_STATUS[i - 1]);
                ps.setString(4, REPORTING_TYPES[i - 1]);
                ps.setInt(5, i); // author_id 1..5
                ps.executeUpdate();
            }
        }

        System.out.println("Tabella reporting popolata.");
    }
}


