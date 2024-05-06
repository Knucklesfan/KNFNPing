package com.knuxstuff.knfnping;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SoundOptions implements ConfigurationSerializable {
    String name;
    String sound;
    ItemStack block;

    @Override
    public String toString() {
        return "SoundOptions{" +
                "name='" + name + '\'' +
                ", sound='" + sound + '\'' +
                ", block=" + block +
                '}';
    }

    public SoundOptions(String name, String sound, String block) {
        this.name = name;
        this.sound = sound;
        this.block = new ItemStack(Material.getMaterial(block),1);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name);
        data.put("sound", this.sound);
        data.put("block", this.block.getType().toString());

        return data;
    }

    public static SoundOptions deserialize(Map<String, Object> args) {
        return new SoundOptions(
                (String) args.get("name"),
                (String) args.get("sound"),
                (String) args.get("block")

        );
    }
}

