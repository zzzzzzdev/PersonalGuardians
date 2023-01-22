package me.skript.guardians.file;

import com.google.common.collect.Maps;
import lombok.Getter;
import me.skript.guardians.guardian.GuardianManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

@Getter
public class FileManager {

    private GuardianManager guardianManager;

    private Map<String, YamlConfiguration> yamlConfigurationMap = Maps.newHashMap();

    public FileManager(GuardianManager guardianManager) {
        this.guardianManager = guardianManager;
        load();
    }

    private void load() {
        File directory = new File(guardianManager.getInstance().getDataFolder().getAbsoluteFile() + "/types/");
        if (!directory.exists()) {
            guardianManager.getInstance().saveResource("types/golem.yml", false);
        }
        try (Stream<Path> paths = java.nio.file.Files.walk(Paths.get(guardianManager.getInstance().getDataFolder().getAbsolutePath() + "/types/"))) {
            paths.filter(java.nio.file.Files::isRegularFile).forEach(file -> {
                String plain = file.getFileName().toString();
                if (plain.contains(".")) plain = plain.substring(0, plain.lastIndexOf('.'));
                getYamlConfigurationMap().put(plain, YamlConfiguration.loadConfiguration(file.toFile()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
