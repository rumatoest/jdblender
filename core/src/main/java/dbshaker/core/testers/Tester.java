package dbshaker.core.testers;

import dbshaker.core.CallbackQuery;
import dbshaker.core.Scores;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * Calculate.
 */
public class Tester {

    /**
     * Perform test N times with time scores calculation.
     *
     * @param idFrom Test case ID
     * @param idToInclusive Test case ID should stop on (must be greater than idFrom)
     * @param callback Callback that will be executed with each test ID in provided range.
     */
    public static Scores test(int idFrom, int idToInclusive, CallbackQuery callback) {
        int heatDelta = (idToInclusive - idFrom) / 10;
        if (heatDelta > 250_000) {
            heatDelta = 250_000;
        }
        if (heatDelta < 2000) {
            heatDelta = 2000;
        }
        int idHeat = idFrom + heatDelta;

        final Scores timer = new Scores();

        IntStream.range(idFrom, idHeat)
            //.parallel()
            .forEach(id -> Tester.queryWithCatch(id, callback, timer));

        int runs = idToInclusive - idHeat;
        long startNs = timer.startNs();
        IntStream.rangeClosed(idHeat, idToInclusive)
            //.parallel()
            .forEach(id -> Tester.queryWithCatch(id, callback, timer));
        timer.stop(runs, startNs);

        return timer;
    }

    static void queryWithCatch(int id, CallbackQuery callback, Scores timer) {
        try {
            timer.updateCode(callback.query(id));
        } catch (Exception ex) {
            Logger.getLogger("ShakerApp").log(Level.SEVERE, ex.getMessage(), ex);
            System.exit(1);
        }
    }

    static long codeIt(long... codes) {
        long sum = 0;
        for (long c : codes) {
            sum += c;
        }
        return sum / codes.length;
    }
}
