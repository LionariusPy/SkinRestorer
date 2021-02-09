package ru.lionarius.skinrestorer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.properties.Property;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class ConfigManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Type hashMapType = new TypeToken<HashMap<UUID, Property>>(){}.getType();

    private HashMap<UUID, Property> skinMap = null;
    private final File configFile;

    public ConfigManager(File configFile) {
        this.configFile = configFile;
        if (!configFile.exists()) configFile.getParentFile().mkdirs();

        try {
            if (!configFile.createNewFile()) {
                FileReader reader = new FileReader(configFile);
                skinMap = gson.fromJson(reader, hashMapType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (skinMap == null) skinMap = new HashMap<>();
    }

    public void addPlayerSkin(UUID player, Property skin) {
        skinMap.put(player, skin);
    }

    public Property getPlayerSkin(UUID player) {
        return skinMap.get(player);
    }

    public void saveMap() {
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            fileWriter.write(gson.toJson(skinMap));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
