package me.skript.guardians.guardian;

import com.google.common.collect.Maps;
import lombok.Getter;
import me.skript.guardians.Guardians;
import me.skript.guardians.file.FileManager;
import net.lucaudev.api.item.ItemBuilder;
import net.lucaudev.api.item.ItemUtils;
import net.lucaudev.api.item.XMaterial;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.Map;
import java.util.UUID;

@Getter
public class GuardianManager {

    private Guardians instance;

    private FileManager fileManager;

    private Map<String, Guardian> stringGuardianMap = Maps.newHashMap();

    public GuardianManager(Guardians instance){
        this.instance = instance;
        fileManager = new FileManager(this);
        load();
    }

    public void load(){
        fileManager.getYamlConfigurationMap().forEach((s, yamlConfiguration) -> {
            Guardian guardian = new Guardian(yamlConfiguration.getInt("Settings.Max Number"),
                    yamlConfiguration.getInt("Settings.Chance"),
                    SpawnType.valueOf(yamlConfiguration.getString("Settings.Spawning").toUpperCase()),
                    new ItemBuilder(XMaterial.matchXMaterial(yamlConfiguration.getString("Spawn Item.Material")).get().parseMaterial()).name(yamlConfiguration.getString("Spawn Item.Name")).appendLore(yamlConfiguration.getStringList("Spawn Item.Lore")).nbt("GUARDIAN", s).nbt("lala", UUID.randomUUID()).build(),
                    getGuardianEntityFromConfig(yamlConfiguration));
            getStringGuardianMap().put(s, guardian);
        });
    }

    private GuardianEntity getGuardianEntityFromConfig(FileConfiguration config){
        return new GuardianEntity(config.getString("Entity.Display"),
                EntityType.valueOf(config.getString("Entity.Type")),
                config.getInt("Entity.Health"),
                config.getStringList("Entity.Potion Effects"),
                getGuardianGear("Helmet", config),
                getGuardianGear("Chestplate", config),
                getGuardianGear("Leggings", config),
                getGuardianGear("Boots", config));
    }

    private ItemStack getGuardianGear(String type, FileConfiguration config){
        return new ItemBuilder(XMaterial.matchXMaterial(config.getString("Entity.Armor." + type + ".Material")).get()).data(config.getInt("Entity.Armor." + type + ".Data")).build();
    }

    public Guardian getGuardianByName(String input){
        return getStringGuardianMap().get(input);
    }

    public Guardian getGuardianFromEgg(ItemStack itemStack){
        return getStringGuardianMap().get(ItemUtils.getNbtString(itemStack, "GUARDIAN"));
    }



}
