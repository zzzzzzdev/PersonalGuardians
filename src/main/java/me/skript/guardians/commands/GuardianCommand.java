package me.skript.guardians.commands;

import me.skript.guardians.Guardians;
import me.skript.guardians.guardian.Guardian;
import net.lucaudev.api.chat.Chat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GuardianCommand implements CommandExecutor {

    private Guardians instance;

    public GuardianCommand(Guardians instance){
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()){

            if(args.length == 0){
                for(String s : Arrays.asList("&6&l[&e!&6&l] &eGuardian Commands", "&e/guardian give (player) (type) &7- CASE SENSITIVE", "&e/guardian list", "&e/guardian reload")){
                    sender.sendMessage(Chat.color(s));
                }
                return false;
            }

            if(args[0].equalsIgnoreCase("give")){
                Player player = Bukkit.getPlayer(args[1]);
                Guardian guardian = instance.getGuardianManager().getGuardianByName(args[2]);
                player.getInventory().addItem(guardian.getSpawnItem());
            } else if(args[0].equalsIgnoreCase("list")){
                sender.sendMessage(Chat.color("&bAvaialble Types: &c" + StringUtils.join(instance.getGuardianManager().getStringGuardianMap().keySet(), ", ")));
            } else if(args[0].equalsIgnoreCase("reload")){
                instance.getGuardianManager().load();
                sender.sendMessage(Chat.color("&bDone!"));
            }
        }

        return true;
    }
}
