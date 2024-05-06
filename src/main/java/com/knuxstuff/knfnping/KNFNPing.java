package com.knuxstuff.knfnping;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class KNFNPing extends JavaPlugin implements Listener {
    boolean playerDefault = false;
    boolean playerHearSelfDefault = false;

    String playerDefaultSound = "";
    boolean chestGraves = false;
    boolean allowHome = false;
    YamlConfiguration sounds;
    List<PlayerOptions> playerOptionsList;
    List<SoundOptions> soundOptionsList;

    @Override
    public void onEnable() {
        saveResource("config.yml", /* replace */ false);
        saveResource("sounds.yml", /* replace */ false);

        super.onEnable();
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage("Starting to load the actual plugin... [KNFN]");
        // You can also use this for configuration files:
        ConfigurationSerialization.registerClass(PlayerOptions.class);
        ConfigurationSerialization.registerClass(SoundOptions.class);

        File file = new File(this.getDataFolder(), "sounds.yml");
        sounds = YamlConfiguration.loadConfiguration(file);
        soundOptionsList= (List<SoundOptions>) sounds.get("sounds");
        if(soundOptionsList == null) {
            soundOptionsList = new ArrayList<SoundOptions>();
        }
//        System.out.println(Arrays.toString(soundOptionsList.toArray()));

        playerDefault = getConfig().getBoolean("player-default");
        playerHearSelfDefault = getConfig().getBoolean("player-hearself-default");
        playerDefaultSound = getConfig().getString("sound-to-play");
        chestGraves = getConfig().getBoolean("chest-graves");
        allowHome = getConfig().getBoolean("allow-home");

        playerOptionsList= (List<PlayerOptions>) getConfig().get("players");
        if(playerOptionsList == null) {
            playerOptionsList = new ArrayList<PlayerOptions>();
        }


        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PingGUI(this), this);
        getServer().getPluginManager().registerEvents(new SoundGUI(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        this.getCommand("pingcli").setExecutor(new PingCommandListener(this));
        this.getCommand("pinggui").setExecutor(new PingGUICommand(this));

        if(playerOptionsList != null) {
//            for(int i = 0; i < playerOptionsList.size(); i++) {
////                System.out.println(playerOptionsList.toArray()[i]);
//            }
        }

        getServer().getConsoleSender().sendMessage(

             "░░░░░▄▄▄▄▀▀▀▀▀▀▀▀▄▄▄▄▄▄░░░░░░░░ [KNFN]\n" +
                "░░░░░█░░░░▒▒▒▒▒▒▒▒▒▒▒▒░░▀▀▄░░░░ [KNFN]\n" +
                "░░░░█░░░▒▒▒▒▒▒░░░░░░░░▒▒▒░░█░░░ [KNFN]\n" +
                "░░░█░░░░░░▄██▀▄▄░░░░░▄▄▄░░░░█░░ [KNFN]\n" +
                "░▄▀▒▄▄▄▒░█▀▀▀▀▄▄█░░░██▄▄█░░░░█░ [KNFN]\n" +
                "█░▒█▒▄░▀▄▄▄▀░░░░░░░░█░░░▒▒▒▒▒░█ [KNFN]\n" +
                "█░▒█░█▀▄▄░░░░░█▀░░░░▀▄░░▄▀▀▀▄▒█ [KNFN]\n" +
                "░█░▀▄░█▄░█▀▄▄░▀░▀▀░▄▄▀░░░░█░░█░ [KNFN]\n" +
                "░░█░░░▀▄▀█▄▄░█▀▀▀▄▄▄▄▀▀█▀██░█░░ [KNFN]\n" +
                "░░░█░░░░██░░▀█▄▄▄█▄▄█▄████░█░░░ [KNFN]\n" +
                "░░░░█░░░░▀▀▄░█░░░█░█▀██████░█░░ [KNFN]\n" +
                "░░░░░▀▄░░░░░▀▀▄▄▄█▄█▄█▄█▄▀░░█░░ [KNFN]\n" +
                "░░░░░░░▀▄▄░▒▒▒▒░░░░░░░░░░▒░░░█░ [KNFN]\n" +
                "░░░░░░░░░░▀▀▄▄░▒▒▒▒▒▒▒▒▒▒░░░░█░ [KNFN]\n" +
                "░░░░░░░░░░░░░░▀▄▄▄▄▄░░░░░░░░█░░ [KNFN]\n" +
                 "\u001B[34mKnuxfan's badass plugin loaded\u001B[0m [KNFN]");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
