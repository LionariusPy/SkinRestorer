package ru.lionarius.skinrestorer;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.SERVER)
public class SkinRestorer implements DedicatedServerModInitializer {

    public final static ConfigManager configManager = new ConfigManager(FabricLoader.getInstance().getConfigDirectory().toPath().resolve("skinrestorer"));
    public final static String defaultSkinName = "Alex";

    @Override
    public void onInitializeServer() {

    }
}
