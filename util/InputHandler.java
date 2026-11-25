package util;

import java.util.Scanner;

/**
 * InputHandler provides safe, reusable methods for reading terminal input.
 *
 * Key design decisions:
 *  - Uses ONE shared Scanner object (passed in from outside) to avoid closing System.in.
 *  - Handles NumberFormatException gracefully and re-prompts the user.
 *  - Trims whitespace from all string inputs.
 *
 * OOP concept demonstrated: Utility class, exception handling
 */
public class InputHandler {

    // The shared Scanner – do NOT close it inside this class
    private Scanner scanner;

    /**
     * Constructor – receives the application-wide Scanner.
     */
    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reads a non-empty string from the terminal.
     * Keeps asking until the user provides at least one character.
     *
     * @param prompt The message shown to the user before reading input
     * @return The trimmed non-empty string the user typed
     */
    public String readString(String prompt) {
        String input = "";
        while (input.isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
        return input;
    }

    /**
     * Reads an optional string (may be blank).
     * Returns an empty string if the user just presses Enter.
     */
    public String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads an integer from the terminal.
     * Keeps asking until the user enters a valid whole number.
     *
     * @param prompt The message shown before reading input
     * @return The integer the user typed
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // Not a valid integer – tell the user and try again
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Reads an integer within a given range [min, max] (inclusive).
     * Keeps asking until the value is within range.
     */
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }
}
