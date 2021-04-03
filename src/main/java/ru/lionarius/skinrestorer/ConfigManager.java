package ru.lionarius.skinrestorer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.lionarius.skinrestorer.util.WebUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class ConfigManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String fileType = ".json";

    private final HashMap<UUID, Property> skinMap = new HashMap<>();
    private final Path configPath;

    public ConfigManager(Path configPath) {
        this.configPath = configPath;
    }

    public void updatePlayerSkin(UUID player, Property skin) {
        skinMap.put(player, skin);
    }

    public Property getPlayerSkin(UUID player) {
        return skinMap.get(player);
    }

    public void onPlayerJoin(ServerPlayerEntity player) {
        if(configPath.resolve(player.getUuid().toString() + fileType).toFile().exists()) {
            try {
                Property parsedProperty = gson.fromJson(new FileReader(configPath.resolve(player.getUuid().toString() + fileType).toFile()), Property.class);
                if(!parsedProperty.getValue().equals("")) {
                    skinMap.put(player.getUuid(), parsedProperty);
                    return;
                }
            } catch (Exception ignored) {}
        }

        try {
            skinMap.put(player.getUuid(), WebUtils.getSkin(WebUtils.getUUID(SkinRestorer.defaultSkinName)));
        } catch (Exception ignored) { }
    }

    public void onPlayerDisconnect(ServerPlayerEntity player) {
        try {

            if(!configPath.toFile().exists()) configPath.toFile().mkdir();
            configPath.resolve(player.getUuid().toString() + fileType).toFile().createNewFile();

            FileWriter fileWriter = new FileWriter(configPath.resolve(player.getUuid().toString() + fileType).toFile());
            fileWriter.write(gson.toJson(skinMap.get(player.getUuid())));
            fileWriter.close();
        } catch (Exception ignored) { }
        skinMap.remove(player.getUuid());
    }
}
