package jfz.booking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Test helper: runs a program's main method with the given keyboard input and
 * returns everything it printed, with line endings normalised to \n. The real
 * System.in, System.out and default locale are always put back, even if main
 * throws.
 *
 * The locale is set to US English while main runs, so printf("%.2f") prints
 * 12.50 with a dot on every computer. (A South African locale prints 12,50.)
 */
final class ConsoleRunner {

    @FunctionalInterface
    interface MainMethod {
        void run(String[] args) throws Exception;
    }

    private ConsoleRunner() {
    }

    static String run(MainMethod main, String stdin) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        Locale originalLocale = Locale.getDefault();
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        try {
            Locale.setDefault(Locale.US);
            System.setIn(new ByteArrayInputStream(stdin.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
            main.run(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            Locale.setDefault(originalLocale);
        }
        return captured.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
