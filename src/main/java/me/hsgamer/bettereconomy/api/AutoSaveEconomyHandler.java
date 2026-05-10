package me.hsgamer.bettereconomy.api;

import me.hsgamer.bettereconomy.BetterEconomy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AutoSaveEconomyHandler extends EconomyHandler implements Runnable {
    private final AtomicBoolean needSaving = new AtomicBoolean();
    private final ScheduledExecutorService executor;

    protected AutoSaveEconomyHandler(BetterEconomy instance) {
        super(instance);
        int period = instance.getMainConfig().getSaveFilePeriod();
        if (period >= 0) {
            long periodMs = period * 50L;
            executor = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "BetterEconomy-AutoSave");
                t.setDaemon(true);
                return t;
            });
            executor.scheduleAtFixedRate(this, periodMs, periodMs, TimeUnit.MILLISECONDS);
        } else {
            executor = null;
        }
    }

    @Override
    public final void run() {
        if (!needSaving.get()) {
            return;
        }
        this.save();
        needSaving.set(false);
    }

    protected abstract void save();

    protected void enableSave() {
        needSaving.lazySet(true);
    }

    @Override
    public void disable() {
        if (executor != null) {
            executor.shutdown();
        }
        save();
    }
}
