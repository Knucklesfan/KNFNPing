package com.knuxstuff.knfnping;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.MetadataValue;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        World w = playerJoinEvent.getPlayer().getWorld();
        Firework fw = (Firework) w.spawnEntity(playerJoinEvent.getPlayer().getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.BLUE).flicker(true).build());
        fw.setFireworkMeta(fwm);
        fw.setTicksToDetonate(5);
        playerJoinEvent.joinMessage(
                Component.text(">", NamedTextColor.GREEN, TextDecoration.BOLD)
                .append(playerJoinEvent.getPlayer().displayName().color(NamedTextColor.AQUA))
                .append(Component.text(" is now online.", NamedTextColor.DARK_GRAY)));
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent playerJoinEvent) {
        playerJoinEvent.quitMessage(
                Component.text(">", NamedTextColor.GREEN, TextDecoration.BOLD)
                        .append(playerJoinEvent.getPlayer().displayName().color(NamedTextColor.AQUA))
                        .append(Component.text(" has disconnected.", NamedTextColor.DARK_GRAY)));
    }

}
