package dbshaker.core;

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
    public static Scores test(int idFrom, int idToInclusive, QueryCallback callback) {
        int heatDelta = (idToInclusive - idFrom) / 10;
        if (heatDelta > 1000000) {
            heatDelta = 1000000;
        }
        int idHeat = idFrom + heatDelta;

        IntStream.range(idFrom, idHeat)
            .parallel()
            .forEach(id -> callback.query(id));

        final Scores timer = new Scores();
        int runs = idToInclusive - idHeat;
        long startNs = timer.startNs();
        IntStream.rangeClosed(idHeat, idToInclusive)
            .parallel()
            .forEach(id -> callback.query(id));
        timer.stop(runs, startNs);

        return timer;
    }

}
