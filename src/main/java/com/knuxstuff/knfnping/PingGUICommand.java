package com.knuxstuff.knfnping;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingGUICommand implements CommandExecutor {
    KNFNPing plugin;
    PingGUICommand(KNFNPing plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String commandString, @NotNull String[] args) {

        if(commandSender instanceof Player) {
            PingGUI myInventory = new PingGUI(plugin);
            Player p = (Player)commandSender;
            myInventory.setUuid(p.getUniqueId());
            p.openInventory(myInventory.getInventory());

        }
        return false;
    }
}
