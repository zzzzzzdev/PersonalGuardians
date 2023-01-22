package me.skript.guardians.listener;

import me.skript.guardians.Guardians;
import me.skript.guardians.guardian.Guardian;
import me.skript.guardians.guardian.SpawnType;
import net.lucaudev.api.chat.Chat;
import net.lucaudev.api.item.ItemUtils;
import net.lucaudev.api.item.XPotion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class GuardianListener implements Listener {

    private Guardians instance;

    public GuardianListener(Guardians instance){
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }

        if(event.getClickedBlock() == null){
            return;
        }

        if(!ItemUtils.isValid(event.getPlayer().getItemInHand())){
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();

        Guardian guardian = instance.getGuardianManager().getGuardianFromEgg(itemStack);

        if(guardian == null){
            return;
        }

        event.setCancelled(true);

        Bukkit.getScheduler().runTaskLater(instance, () -> {
            int amount = guardian.getSpawnType() == SpawnType.CONSTANT ? guardian.getMaxNumber() : ThreadLocalRandom.current().nextInt(1, guardian.getMaxNumber());
            for(int i = 0; i < amount; i++){
                spawnGuardian(player, guardian);
            }

            if(itemStack.getAmount() == 1){
                player.setItemInHand(null);
            } else {
                itemStack.setAmount(itemStack.getAmount() - 1);
            }
        }, 10L);
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event){
        if(event.getTarget() instanceof Player && event.getEntity().hasMetadata("custom_entity_personal")){
            event.setCancelled(true);
        }

        if(event.getTarget() instanceof Player && !event.getEntity().hasMetadata("custom_entity_personal")){
            Player player = (Player) event.getTarget();
            for (Entity nearbyEntity : player.getNearbyEntities(32, 32, 32)) {
                if(nearbyEntity.hasMetadata("custom_entity_personal")){
                    event.setTarget(nearbyEntity);
                }
            }
        }
    }

    private void spawnGuardian(Player player, Guardian guardian){
        LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), guardian.getGuardianEntity().getEntityType());
        entity.setCustomName(Chat.color(guardian.getGuardianEntity().getDisplayName()));
        entity.setCustomNameVisible(true);
        entity.setMaxHealth(guardian.getGuardianEntity().getHealth());
        entity.setHealth(guardian.getGuardianEntity().getHealth());
        entity.getEquipment().setHelmet(guardian.getGuardianEntity().getHelmetItem());
        entity.getEquipment().setChestplate(guardian.getGuardianEntity().getChestplateItem());
        entity.getEquipment().setLeggings(guardian.getGuardianEntity().getLeggingsItem());
        entity.getEquipment().setBoots(guardian.getGuardianEntity().getBootsItem());
        entity.setMetadata("custom_entity_personal", new FixedMetadataValue(instance, true));
        for (String potionEffect : guardian.getGuardianEntity().getPotionEffects()) {
            Optional<XPotion> potionEffectType = XPotion.matchXPotion(potionEffect.split(":")[0]);
            int level = Integer.parseInt(potionEffect.split(":")[1]);
            long time = Long.parseLong(potionEffect.split(":")[2]);
            entity.addPotionEffect(new PotionEffect(potionEffectType.get().parsePotionEffectType(), (int) time * 20, level));
        }
    }


}
