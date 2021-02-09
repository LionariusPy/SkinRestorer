package ru.lionarius.skinrestorer.mixin;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.lionarius.skinrestorer.SkinRestorer;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin {

    @Inject(method = "shutdown", at = @At("TAIL"))
    private void shutdown(CallbackInfo ci) {
        SkinRestorer.configManager.saveMap();
    }
}
