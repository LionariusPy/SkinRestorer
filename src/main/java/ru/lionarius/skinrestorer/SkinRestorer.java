package ru.lionarius.skinrestorer;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.PlayerManager;

@Environment(EnvType.SERVER)
public class SkinRestorer implements DedicatedServerModInitializer {

    public static PlayerManager PLAYER_MANAGER;
    public final static ConfigManager configManager = new ConfigManager(FabricLoader.getInstance().getConfigDirectory().toPath().resolve("skinrestorer\\skins.json").toFile());

    @Override
    public void onInitializeServer() {

    }
}
