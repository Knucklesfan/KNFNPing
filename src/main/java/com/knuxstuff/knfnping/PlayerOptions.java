package com.knuxstuff.knfnping;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlayerOptions implements ConfigurationSerializable {
    private Integer homeX;
    private Integer homeY;
    private Integer homeZ;

    boolean playPing = false;
    boolean chestGrave = false;
    boolean hearSelf = false;
    String pingSFX = "";
    String nickname = "";
    String uuid = "";
    public PlayerOptions(String name, String uuid, KNFNPing p) {
        this.homeX = Integer.MAX_VALUE;
        this.homeY = Integer.MAX_VALUE;
        this.homeZ = Integer.MAX_VALUE;
        this.playPing = p.playerDefault;
        this.chestGrave = p.chestGraves;
        this.pingSFX = p.playerDefaultSound;
        this.hearSelf = p.playerHearSelfDefault;
        this.nickname = name;
        this.uuid = uuid;

    }
    public PlayerOptions(int homeX, int homeY, int homeZ, boolean playPing, boolean chestGrave, boolean hearSelf, String pingSFX, String nickname, String uuid) {
        this.homeX = homeX;
        this.homeY = homeY;
        this.homeZ = homeZ;
        this.playPing = playPing;
        this.chestGrave = chestGrave;
        this.pingSFX = pingSFX;
        this.nickname = nickname;
        this.uuid = uuid;
        this.hearSelf = hearSelf;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("play-ping", this.playPing);
        data.put("chest-grave", this.chestGrave);
        data.put("ping-sound", this.pingSFX);
        data.put("hear-self", this.hearSelf);

        data.put("nickname", this.nickname);
        data.put("home-x", homeX);
        data.put("home-y", homeY);
        data.put("home-z", homeZ);
        data.put("uuid", uuid);

        return data;
    }

    public static PlayerOptions deserialize(Map<String, Object> args) {
        return new PlayerOptions(
                (int) args.get("home-x"),
                (int) args.get("home-y"),
                (int) args.get("home-z"),
                (boolean) args.get("play-ping"),
                (boolean) args.get("chest-grave"),
                (boolean) args.get("hear-self"),
                (String) args.get("ping-sound"),
                (String) args.get("nickname"),
                (String) args.get("uuid")
                );
    }
}

