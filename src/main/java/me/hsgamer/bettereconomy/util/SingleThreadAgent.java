package me.hsgamer.bettereconomy.util;

import me.hsgamer.topper.agent.core.Agent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SingleThreadAgent implements Agent {
    private final Runnable runnable;
    private final long periodMs;
    private final ScheduledExecutorService executor;

    public SingleThreadAgent(Runnable runnable, String threadName, long periodTicks) {
        this.runnable = runnable;
        this.periodMs = periodTicks * 50L;
        this.executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, threadName);
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public void start() {
        executor.scheduleAtFixedRate(runnable, periodMs, periodMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        executor.shutdown();
    }
}
