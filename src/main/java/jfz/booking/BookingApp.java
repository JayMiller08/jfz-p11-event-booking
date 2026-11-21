package jfz.booking;

import java.util.Scanner;

/** The box office menu. Nothing the user types can crash it. */
public class BookingApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Venue venue = new Venue(3, 4);
        // TODO 8: the menu loop from the brief. Use readNumber for every answer,
        //         and catch both exceptions when booking or cancelling.
    }

    /** Asks until the user types a whole number. */
    static int readNumber(Scanner input, String prompt) {
        return 0; // TODO 9: lesson 11.5's pattern: nextLine, parseInt, catch, loop
    }
}
