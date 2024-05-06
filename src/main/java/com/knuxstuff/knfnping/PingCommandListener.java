package com.knuxstuff.knfnping;

import net.kyori.adventure.identity.Identity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PingCommandListener implements CommandExecutor {
    KNFNPing plugin;
    PingCommandListener(KNFNPing plugin) {
        this.plugin = plugin;
    }
    private int getPlayer(String player) {
       // System.out.println(player);
        if(plugin.playerOptionsList == null) {
            return -1;
        }
        //might as well implement a binary search if we gonna be this slow lol
        int l = 0, r = plugin.playerOptionsList.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            // Check if x is present at mid
            if (plugin.playerOptionsList.get(m).uuid.equals(player))
                return m;

            // If x greater, ignore left half
            if (plugin.playerOptionsList.get(m).uuid.compareTo(player) < 0)
                l = m + 1;

                // If x is smaller, ignore right half
            else
                r = m - 1;
        }

        return -1;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UUID entityID = player.getUniqueId();

            if (getPlayer(entityID.toString()) == -1) {
                plugin.playerOptionsList.add(new PlayerOptions(player.getName(), entityID.toString(), plugin));
            }
            if(args.length > 0) {
                if (args[0].equals("togglesounds")) {
                    plugin.playerOptionsList.get(getPlayer(entityID.toString())).playPing = !plugin.playerOptionsList.get(getPlayer(entityID.toString())).playPing;
                    player.sendMessage("Toggled message sounds.");
                    plugin.saveConfig();
                } else if (args[0].equals("toggleself")) {
                    plugin.playerOptionsList.get(getPlayer(entityID.toString())).hearSelf = !plugin.playerOptionsList.get(getPlayer(entityID.toString())).hearSelf;
                    player.sendMessage("Toggled self-pinging sounds.");
                    plugin.saveConfig();
                } else if (args[0].equals("sound")) {
                    plugin.playerOptionsList.get(getPlayer(entityID.toString())).pingSFX = args[1];
                    player.sendMessage("Set message sound.");
                    plugin.saveConfig();
                } else {
                    player.sendMessage("Syntax:\n" +
                            "pingcli togglesounds - Toggles the sounds for all chat messages\n" +
                            "pingcli toggleself - Toggles the sounds for just messages sent by you\n" +
                            "pingcli sound <SOUNDID> - Sets the sound id of any message. (See https://minecraft.wiki/w/Sounds.json)\n");
                }
                plugin.getConfig().set("players",plugin.playerOptionsList);
                plugin.saveConfig();

            }
            else {
                player.sendMessage("Syntax:\n" +
                        "pingcli togglesounds - Toggles the sounds for all chat messages\n" +
                        "pingcli toggleself - Toggles the sounds for just messages sent by you\n" +
                        "pingcli sound <SOUNDID> - Sets the sound id of any message. (See https://minecraft.wiki/w/Sounds.json)\n");
            }
            return true;
        }
        return false;

    }
}
