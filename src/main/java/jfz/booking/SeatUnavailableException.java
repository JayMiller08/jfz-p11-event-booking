package jfz.booking;

/** Checked: the seat exists, but can't be booked (or cancelled) right now. */
public class SeatUnavailableException extends Exception {
    private static final long serialVersionUID = 1L;

    public SeatUnavailableException(String message) {
        super(message);
    }
}
