package com.knuxstuff.knfnping;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SoundGUI implements InventoryHolder, Listener {
    private final Inventory mainInv;
    UUID uuid;

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private PlayerOptions getPlayer(String player) {
//        System.out.println(player);
        if(plugin.playerOptionsList == null) {
            return null;
        }
        //might as well implement a binary search if we gonna be this slow lol
        int l = 0, r = plugin.playerOptionsList.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            // Check if x is present at mid
            if (plugin.playerOptionsList.get(m).uuid.equals(player))
                return plugin.playerOptionsList.get(m);

            // If x greater, ignore left half
            if (plugin.playerOptionsList.get(m).uuid.compareTo(player) < 0)
                l = m + 1;

                // If x is smaller, ignore right half
            else
                r = m - 1;
        }

        return null;
    }


    private final KNFNPing plugin;
    public SoundGUI(KNFNPing plugin) {
        this.plugin = plugin;
        // Create an Inventory with 9 slots, `this` here is our InventoryHolder.
        this.mainInv = plugin.getServer().createInventory(this, 27,Component.text("Select a sound..."));
        for(int i = 0; i < plugin.soundOptionsList.size(); i++) {
            String soundText = plugin.soundOptionsList.get(i).name;
            if(soundText == null) {

            }
            final TextComponent playsounds = Component.text(soundText).decorate(TextDecoration.BOLD).color(NamedTextColor.GREEN);
            ItemMeta enablePlayStackItemMeta = plugin.soundOptionsList.get(i).block.getItemMeta();
            enablePlayStackItemMeta.displayName(playsounds);
            plugin.soundOptionsList.get(i).block.setItemMeta(enablePlayStackItemMeta);

            mainInv.setItem(i,plugin.soundOptionsList.get(i).block);

        }

    }

    @Override
    public @NotNull Inventory getInventory() {
        updateInv(uuid);

        return this.mainInv;
    }
    public void updateInv(UUID uuid) {
    }
    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        Inventory inventory = event.getInventory();
        // Add a null check in case the player clicked outside the window.
        if (!(inventory.getHolder(false) instanceof SoundGUI pingGUI)) {
            return;
        }

        event.setCancelled(true);

    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        // Add a null check in case the player clicked outside the window.
        if (inventory == null || !(inventory.getHolder(false) instanceof SoundGUI pingGUI)) {
            return;
        }

        event.setCancelled(true);

        int clicked = event.getSlot();
//        System.out.println(clicked);
        PlayerOptions player = getPlayer(event.getWhoClicked().getUniqueId().toString());
        if(player != null) {
            if(clicked < plugin.soundOptionsList.size()) {
                player.pingSFX = plugin.soundOptionsList.get(clicked).sound;
                Sound musicDisc = Sound.sound(Key.key(player.pingSFX), Sound.Source.PLAYER, 1f, 1f);
                event.getWhoClicked().playSound(musicDisc);
            }
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        // Add a null check in case the player clicked outside the window.
        if (!(inventory.getHolder(false) instanceof SoundGUI soundGUI)
                && !(inventory.getHolder(false) instanceof PingGUI pingGUI)) {
            return;
        }
        plugin.getConfig().set("players",plugin.playerOptionsList);
        plugin.saveConfig();

    }
 }


