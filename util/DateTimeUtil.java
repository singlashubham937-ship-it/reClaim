package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTimeUtil provides simple date and time helper methods.
 *
 * Uses Java's built-in LocalDate and LocalDateTime (Java 8+, also valid in Java 21).
 * All methods are static so you can call them without creating an object.
 *
 * OOP concept demonstrated: Utility class with static methods
 */
public class DateTimeUtil {

    // Formatter for dates like: 25-Nov-2026
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    // Formatter for date+time like: 25-Nov-2026 17:30:00
    private static final DateTimeFormatter DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    // Private constructor – should not be instantiated
    private DateTimeUtil() {}

    /**
     * Returns today's date as a LocalDate object.
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Returns the current date AND time as a LocalDateTime object.
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Formats a LocalDate as a readable string, e.g. "25-Nov-2026".
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(DATE_FORMAT);
    }

    /**
     * Formats a LocalDateTime as a readable string, e.g. "25-Nov-2026 17:30:00".
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(DATETIME_FORMAT);
    }

    /**
     * Returns today's date as a formatted string – convenient shortcut.
     */
    public static String todayAsString() {
        return formatDate(getCurrentDate());
    }

    /**
     * Returns the current date and time as a formatted string – convenient shortcut.
     */
    public static String nowAsString() {
        return formatDateTime(getCurrentDateTime());
    }
}
