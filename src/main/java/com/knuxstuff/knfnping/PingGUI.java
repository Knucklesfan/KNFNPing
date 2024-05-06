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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PingGUI implements InventoryHolder, Listener {
    private final ItemStack is = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
    private final ItemMeta itemmeta = is.getItemMeta();
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
    public PingGUI(KNFNPing plugin) {
        this.plugin = plugin;
        // Create an Inventory with 9 slots, `this` here is our InventoryHolder.
        this.mainInv = plugin.getServer().createInventory(this, 9, Component.text("User Sound Settings"));
        itemmeta.displayName(Component.text(""));
        final TextComponent craftingtext = Component.text()
                .append(Component.text("Change Message Sound", NamedTextColor.AQUA, TextDecoration.BOLD)).build();
        ItemStack crafting = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta craftingMeta = crafting.getItemMeta();
        craftingMeta.displayName(craftingtext);
        crafting.setItemMeta(craftingMeta);
        is.setItemMeta(itemmeta);
        mainInv.setItem(0,is);
        mainInv.setItem(1,is);
        mainInv.setItem(2,is);

        mainInv.setItem(5,crafting);

        mainInv.setItem(6,is);
        mainInv.setItem(7,is);
        mainInv.setItem(8,is);
    }

    @Override
    public @NotNull Inventory getInventory() {
        updateInv(uuid);

        return this.mainInv;
    }
    public void updateInv(UUID uuid) {
        if(getPlayer(uuid.toString()) == null) {
            plugin.playerOptionsList.add(new PlayerOptions("", uuid.toString(), plugin));
        }
        PlayerOptions player = getPlayer(uuid.toString());
        String enablePlayString = "Enable";
        NamedTextColor enablePlayColor = NamedTextColor.RED;
        ItemStack enablePlayStack  = new ItemStack(Material.RED_WOOL);

        if(player.playPing) {
            enablePlayString = "Disable";
            enablePlayColor = NamedTextColor.GREEN;
            enablePlayStack = new ItemStack(Material.GREEN_WOOL);

        }

        String enableHearString = "Enable";
        NamedTextColor enableHearColor = NamedTextColor.RED;
        ItemStack enableHearStack  = new ItemStack(Material.RED_WOOL);

        if(player.hearSelf) {
            enableHearString = "Disable";
            enableHearColor = NamedTextColor.GREEN;
            enableHearStack = new ItemStack(Material.GREEN_WOOL);

        }
        final TextComponent hearsounds = Component.text()
                .append(Component.text(enableHearString+" Self-sent Sounds", enableHearColor, TextDecoration.BOLD)).build();
        ItemMeta enableHearStackItemMeta = enableHearStack.getItemMeta();
        enableHearStackItemMeta.displayName(hearsounds);
        enableHearStack.setItemMeta(enableHearStackItemMeta);

        final TextComponent playsounds = Component.text()
                .append(Component.text(enablePlayString+" Play Sounds", enablePlayColor, TextDecoration.BOLD)).build();
        ItemMeta enablePlayStackItemMeta = enablePlayStack.getItemMeta();
        enablePlayStackItemMeta.displayName(playsounds);
        enablePlayStack.setItemMeta(enablePlayStackItemMeta);

        mainInv.setItem(3,enablePlayStack);
        mainInv.setItem(4,enableHearStack);


    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        // Add a null check in case the player clicked outside the window.
        if (inventory == null || !(inventory.getHolder(false) instanceof PingGUI pingGUI)) {
            return;
        }

        event.setCancelled(true);

        int clicked = event.getSlot();
//        System.out.println(clicked);
        PlayerOptions player = getPlayer(event.getWhoClicked().getUniqueId().toString());
        if(player != null) {

            switch(clicked) {
                case 3: {
                    player.playPing = !player.playPing;
                    Sound musicDisc = Sound.sound(Key.key(player.pingSFX), Sound.Source.PLAYER, 1f, 1f);
                    event.getWhoClicked().playSound(musicDisc);
                    plugin.getConfig().set("players",plugin.playerOptionsList);
                    plugin.saveConfig();

                    ((PingGUI)inventory.getHolder(false)).updateInv(event.getWhoClicked().getUniqueId());
                }break;
                case 4: {
                    player.hearSelf = ! player.hearSelf;
                    Sound musicDisc = Sound.sound(Key.key(player.pingSFX), Sound.Source.PLAYER, 1f, 1f);
                    event.getWhoClicked().playSound(musicDisc);
                    plugin.getConfig().set("players",plugin.playerOptionsList);
                    plugin.saveConfig();

                    ((PingGUI)inventory.getHolder(false)).updateInv(event.getWhoClicked().getUniqueId());
                }break;
                case 5: {
                    SoundGUI soundGUI = new SoundGUI(plugin);
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(soundGUI.getInventory());
                }break;

                default:
                    break;
            }
        }
        // Check if the player clicked the stone.
//        if (clicked != null && clicked.getType() == Material.STONE) {
//            // Use the method we have on MyInventory to increment the field
//            // and update the counter.
//            myInventory.addClick();
//        }
    }

}
