package jdblender.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/**
 *
 */
public class Scores {

    long hash;

    LongAdder iteration = new LongAdder();

    DoubleAdder runTimeNs = new DoubleAdder();

    String label = "n/a";

    public void label(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public long startNs() {
        return System.nanoTime();
    }

    public void stop(int runsCount, long startNs) {
        iteration.add(runsCount);
        runTimeNs.add(System.nanoTime() - startNs);
    }

    public int iterations() {
        return iteration.intValue();
    }

    /**
     * Time in microseconds.
     */
    public double avgMu() {
        return (double)TimeUnit.NANOSECONDS.toMicros(runTimeNs.longValue()) / iteration.intValue();
    }

    public double timeSec() {
        return (double)TimeUnit.NANOSECONDS.toMillis(runTimeNs.longValue()) / 1000;
    }

    public void updateCode(long hash) {
        this.hash = this.hash / 2 + hash / 2;
    }

    long getCode() {
        return this.hash;
    }
}
