package me.skript.guardians;

import lombok.Getter;
import me.skript.guardians.commands.GuardianCommand;
import me.skript.guardians.guardian.GuardianManager;
import me.skript.guardians.listener.GuardianListener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Guardians extends JavaPlugin {

    @Getter
    private static Guardians instance;

    private GuardianManager guardianManager;

    @Override
    public void onEnable() {
        instance = this;
        guardianManager = new GuardianManager(this);
        getCommand("guardian").setExecutor(new GuardianCommand(this));
        getServer().getPluginManager().registerEvents(new GuardianListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
