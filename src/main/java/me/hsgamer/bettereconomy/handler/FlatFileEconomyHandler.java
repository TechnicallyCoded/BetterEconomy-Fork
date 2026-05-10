package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.AutoSaveEconomyHandler;
import me.hsgamer.bettereconomy.util.AtomicYamlSave;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathString;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class FlatFileEconomyHandler extends AutoSaveEconomyHandler {
    private final BukkitConfig config;
    private final File configFile;

    public FlatFileEconomyHandler(BetterEconomy instance) {
        super(instance);
        this.config = new BukkitConfig(instance, "balances.yml");
        this.configFile = new File(instance.getDataFolder(), "balances.yml");
        config.setup();
    }

    @Override
    protected void save() {
        try {
            AtomicYamlSave.save(config.getOriginal(), configFile);
        } catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "Failed to save balances.yml", e);
        }
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return config.contains(new PathString(uuid.toString()));
    }

    @Override
    public double get(UUID uuid) {
        return config.getInstance(new PathString(uuid.toString()), 0, Number.class).doubleValue();
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < instance.getMainConfig().getMinimumAmount()) {
            return false;
        }
        config.set(new PathString(uuid.toString()), amount);
        enableSave();
        return true;
    }

    @Override
    public boolean createAccount(UUID uuid, double startAmount) {
        if (hasAccount(uuid)) {
            return false;
        }
        config.set(new PathString(uuid.toString()), startAmount);
        enableSave();
        return true;
    }
}
