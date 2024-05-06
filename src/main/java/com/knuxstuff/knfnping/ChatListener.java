package com.knuxstuff.knfnping;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class ChatListener implements Listener, ChatRenderer { // Implement the ChatRenderer and Listener interface
    private final KNFNPing plugin;
    private int getPlayer(String player) {
//        System.out.println(player);
        if(plugin.playerOptionsList == null) {

            return -1;
        }
        //might as well implement a binary search if we gonna be this slow lol
        for (int m = 0; m < plugin.playerOptionsList.size(); m++) {
            // Check if x is present at mid
            if (plugin.playerOptionsList.get(m).uuid.equals(player))
                return m;
        }

        return -1;
    }
    ChatListener(KNFNPing plug) {
        this.plugin = plug;
    }
    // Listen for the AsyncChatEvent
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        for(Audience e : event.viewers()) {
            UUID entityID = e.get(Identity.UUID).orElse(null);
            if(entityID != null) {
//                System.out.println(event.getPlayer().getUniqueId().toString());
                if(
                                getPlayer(entityID.toString()) == -1
                )
                {
                    if(plugin.playerDefault) {
                        if(!(entityID.toString().equals(event.getPlayer().getUniqueId().toString()))
                                || plugin.playerHearSelfDefault) {
                            String pingSFX = plugin.playerDefaultSound;
                            Sound musicDisc = Sound.sound(Key.key(pingSFX), Sound.Source.PLAYER, 1f, 2f);
                            e.playSound(musicDisc);

                        }

                    }

                }else
                if(
                        (
                                !(entityID.toString().equals(event.getPlayer().getUniqueId().toString()))
                                || plugin.playerOptionsList.get(getPlayer(entityID.toString())).hearSelf)) {
                    if(plugin.playerOptionsList.get(getPlayer(entityID.toString())).playPing) {
                        String pingSFX = plugin.playerOptionsList.get(getPlayer(entityID.toString())).pingSFX;
                        Sound musicDisc = Sound.sound(Key.key(pingSFX), Sound.Source.PLAYER, 1f, 2f);
                        e.playSound(musicDisc);

                    }
                }
            }
        }
        event.renderer(this); // Tell the event to use our renderer
    }

    // Override the render method

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        return sourceDisplayName
                .append(Component.text(": "))
                .append(message);
    }
}