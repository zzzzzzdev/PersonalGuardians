package me.skript.guardians.guardian;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter @AllArgsConstructor
public class GuardianEntity {

    private String displayName;

    private EntityType entityType;

    private int health;

    private List<String> potionEffects;

    private ItemStack helmetItem;

    private ItemStack chestplateItem;

    private ItemStack leggingsItem;

    private ItemStack bootsItem;

}
