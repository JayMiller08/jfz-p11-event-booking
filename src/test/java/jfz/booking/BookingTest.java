package jfz.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * assertThrows(SomeException.class, () -> code) passes only if the code throws
 * that exception, and gives it back so its message can be checked. Module 12
 * explains it properly.
 */
class BookingTest {

    @Test
    @DisplayName("SeatUnavailableException is checked; InvalidSeatException is unchecked")
    void exceptionKinds() {
        assertTrue(Exception.class.isAssignableFrom(SeatUnavailableException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(SeatUnavailableException.class),
                "SeatUnavailableException should extend Exception, so callers must handle it.");
        assertTrue(RuntimeException.class.isAssignableFrom(InvalidSeatException.class),
                "InvalidSeatException should extend RuntimeException: asking for a seat that doesn't exist is a mistake.");
    }

    @Test
    @DisplayName("A new venue has every seat free")
    void newVenue() {
        Venue venue = new Venue(3, 4);
        assertEquals(3, venue.rows());
        assertEquals(4, venue.seatsPerRow());
        assertEquals(12, venue.freeCount(), "3 rows of 4 seats is 12 seats.");
        assertEquals(0, venue.bookedCount());
        assertFalse(venue.isBooked(1, 1), "Seats start free.");
    }

    @Test
    @DisplayName("A venue needs at least 1 row and 1 seat per row")
    void badVenue() {
        assertThrows(IllegalArgumentException.class, () -> new Venue(0, 4), "0 rows makes no sense.");
        assertThrows(IllegalArgumentException.class, () -> new Venue(3, 0), "0 seats per row makes no sense.");
    }

    @Test
    @DisplayName("Booking marks the seat, using numbers from 1")
    void book() throws Exception {
        Venue venue = new Venue(3, 4);
        venue.book(1, 3);
        venue.book(3, 4);
        assertTrue(venue.isBooked(1, 3), "Row 1, seat 3 is the third seat of the first row: index [0][2].");
        assertTrue(venue.isBooked(3, 4), "Row 3, seat 4 is the last seat: index [2][3].");
        assertEquals(2, venue.bookedCount());
        assertEquals(10, venue.freeCount());
    }

    @Test
    @DisplayName("Booking a booked seat throws SeatUnavailableException with a clear message")
    void doubleBooking() throws Exception {
        Venue venue = new Venue(3, 4);
        venue.book(1, 3);
        SeatUnavailableException e = assertThrows(SeatUnavailableException.class, () -> venue.book(1, 3));
        assertEquals("Row 1, seat 3 is already booked", e.getMessage());
        assertEquals(1, venue.bookedCount(), "The failed booking must not change anything.");
    }

    @Test
    @DisplayName("Cancelling frees a booked seat, and refuses a free one")
    void cancel() throws Exception {
        Venue venue = new Venue(3, 4);
        venue.book(2, 2);
        venue.cancel(2, 2);
        assertFalse(venue.isBooked(2, 2));
        SeatUnavailableException e = assertThrows(SeatUnavailableException.class, () -> venue.cancel(2, 2));
        assertEquals("Row 2, seat 2 isn't booked", e.getMessage());
    }

    @Test
    @DisplayName("Seats outside the grid throw InvalidSeatException, for every method")
    void invalidSeats() {
        Venue venue = new Venue(3, 4);
        InvalidSeatException e = assertThrows(InvalidSeatException.class, () -> venue.book(4, 1));
        assertEquals("No such seat: row 4, seat 1", e.getMessage(), "There are only 3 rows.");
        assertThrows(InvalidSeatException.class, () -> venue.book(0, 1), "Rows start at 1.");
        assertThrows(InvalidSeatException.class, () -> venue.cancel(1, 5), "There are only 4 seats per row.");
        assertThrows(InvalidSeatException.class, () -> venue.isBooked(1, 0), "Seats start at 1.");
        assertEquals(0, venue.bookedCount());
    }

    @Test
    @DisplayName("seatingPlan shows X for booked seats and . for free ones")
    void seatingPlan() throws Exception {
        Venue venue = new Venue(2, 3);
        venue.book(1, 2);
        venue.book(2, 1);
        venue.book(2, 3);
        assertEquals("Row 1: .X.\nRow 2: X.X\n", venue.seatingPlan(), "One line per row, each ending with a new line.");
    }

    @Test
    @DisplayName("The menu books, refuses, shows the plan and cancels")
    void menuSession() throws Exception {
        String typed = "2\n1\n3\n2\n1\n3\n1\n3\n1\n3\n4\n";
        String output = ConsoleRunner.run(BookingApp::main, typed);
        String menu = "1) Show seats  2) Book  3) Cancel  4) Quit\nChoice: ";
        assertEquals(menu + "Row: Seat: Booked row 1, seat 3.\n"
                + menu + "Row: Seat: Sorry: Row 1, seat 3 is already booked\n"
                + menu + "Row 1: ..X.\nRow 2: ....\nRow 3: ....\n"
                + menu + "Row: Seat: Cancelled row 1, seat 3.\n"
                + menu + "Goodbye!\n", output,
                "Compare with the example session in the brief.");
    }

    @Test
    @DisplayName("The menu survives words, blanks, unknown choices and seats that don't exist")
    void menuSurvivesAnything() throws Exception {
        String typed = "abc\n\n7\n2\n9\nx\n1\n3\n2\n2\n4\n";
        String output = ConsoleRunner.run(BookingApp::main, typed);
        assertTrue(output.contains("Choice: Please type a whole number.\nChoice: Please type a whole number.\n"),
                "'abc' and an empty line aren't numbers: say so and ask again. Got: " + output);
        assertTrue(output.contains("Choose 1, 2, 3 or 4."), "7 isn't on the menu. Got: " + output);
        assertTrue(output.contains("Row: Seat: Please type a whole number.\nSeat: Sorry: No such seat: row 9, seat 1\n"),
                "Row 9 doesn't exist, and 'x' isn't a seat number. Got: " + output);
        assertTrue(output.contains("Sorry: Row 2, seat 2 isn't booked"), "Cancelling a free seat is refused. Got: " + output);
        assertTrue(output.endsWith("Goodbye!\n"), "4 quits. Got: " + output);
    }
}
