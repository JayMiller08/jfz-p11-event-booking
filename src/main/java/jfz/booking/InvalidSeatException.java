package jfz.booking;

/** Unchecked: the caller asked for a seat that doesn't exist. */
public class InvalidSeatException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // TODO 1: add a constructor that takes a message and passes it to super,
    //         like SeatUnavailableException's.
}
