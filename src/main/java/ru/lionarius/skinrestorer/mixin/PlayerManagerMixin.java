package ru.lionarius.skinrestorer.mixin;

import com.mojang.authlib.properties.Property;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.lionarius.skinrestorer.SkinRestorer;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(MinecraftServer server, DynamicRegistryManager.Impl registryManager, WorldSaveHandler saveHandler, int maxPlayers, CallbackInfo ci) {
        SkinRestorer.PLAYER_MANAGER = (PlayerManager) ((Object) this);
    }

    @Inject(method = "onPlayerConnect", at = @At(value = "HEAD"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        SkinRestorer.configManager.onPlayerJoin(player);
        applySkin(player, SkinRestorer.configManager.getPlayerSkin(player.getUuid()));
    }

    @Inject(method = "remove", at = @At("TAIL"))
    private void remove(ServerPlayerEntity player, CallbackInfo ci) {
        SkinRestorer.configManager.onPlayerDisconnect(player);
    }

    @Inject(method = "disconnectAllPlayers", at = @At("HEAD"))
    private void disconnectAllPlayers(CallbackInfo ci) {
        for (ServerPlayerEntity player : SkinRestorer.PLAYER_MANAGER.getPlayerList()) {
            SkinRestorer.configManager.onPlayerDisconnect(player);
        }
    }

    private static void applySkin(ServerPlayerEntity playerEntity, Property skin) {
        playerEntity.getGameProfile().getProperties().removeAll("textures");
        playerEntity.getGameProfile().getProperties().put("textures", skin);
    }
}
