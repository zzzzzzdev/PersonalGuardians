package me.skript.guardians.guardian;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter @AllArgsConstructor @Setter
public class Guardian {

    private int maxNumber;

    private int chance;

    private SpawnType spawnType;

    private ItemStack spawnItem;

    private GuardianEntity guardianEntity;

}
