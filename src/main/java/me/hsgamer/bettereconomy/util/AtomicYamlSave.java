package me.hsgamer.bettereconomy.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class AtomicYamlSave {
    private AtomicYamlSave() {}

    public static void save(YamlConfiguration yaml, File target) throws IOException {
        File tmp = File.createTempFile(target.getName(), ".tmp", target.getParentFile());
        try {
            Files.write(tmp.toPath(), yaml.saveToString().getBytes(StandardCharsets.UTF_8));
            try {
                Files.move(tmp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            Files.deleteIfExists(tmp.toPath());
            throw e;
        }
    }
}
