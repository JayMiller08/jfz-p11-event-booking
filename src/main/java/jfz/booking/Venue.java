package jfz.booking;

/** A grid of seats. Rows and seats are numbered from 1, like a real venue. */
public class Venue {
    // TODO 2: a private 2D array that records which seats are booked.

    public Venue(int rows, int seatsPerRow) {
        // TODO 3: refuse fewer than 1 row or 1 seat per row with an
        //         IllegalArgumentException, then create the grid.
    }

    public int rows() {
        return 0; // TODO
    }

    public int seatsPerRow() {
        return 0; // TODO
    }

    public boolean isBooked(int row, int seat) {
        return false; // TODO 4: throws InvalidSeatException for a seat that doesn't exist
    }

    public void book(int row, int seat) throws SeatUnavailableException {
        // TODO 5
    }

    public void cancel(int row, int seat) throws SeatUnavailableException {
        // TODO 6
    }

    public int bookedCount() {
        return 0; // TODO
    }

    public int freeCount() {
        return 0; // TODO
    }

    /** One line per row, like "Row 1: ..X.", where X is booked and . is free. */
    public String seatingPlan() {
        return ""; // TODO 7
    }
}
