package dbshaker.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class Scores {

    AtomicInteger iteration = new AtomicInteger();

    long startTime = 0;

    long stopTime = 0;

    String label = "n/a";

    public Scores() {
        this.label = label;
    }

    public void label(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public void start() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
    }

    public void stop() {
        iteration.incrementAndGet();
        this.stopTime = System.currentTimeMillis();
    }

    public void stop(int iterationsPassed) {
        iteration.addAndGet(iterationsPassed);
        this.stopTime = System.currentTimeMillis();
    }

    public int iterations() {
        return iteration.get();
    }

    public double avg() {
        long delta = stopTime - startTime;
        return ((double)delta / iteration.get());
    }

    public int timeMs() {
        long delta = stopTime - startTime;
        return (int)delta / 1000;
    }
}
