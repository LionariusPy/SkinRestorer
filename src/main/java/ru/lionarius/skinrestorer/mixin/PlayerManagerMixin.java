package ru.lionarius.skinrestorer.mixin;

import com.mojang.authlib.properties.Property;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.lionarius.skinrestorer.SkinRestorer;

import java.util.List;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow public abstract List<ServerPlayerEntity> getPlayerList();

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
        for (ServerPlayerEntity player : this.getPlayerList()) {
            SkinRestorer.configManager.onPlayerDisconnect(player);
        }
    }

    private static void applySkin(ServerPlayerEntity playerEntity, Property skin) {
        playerEntity.getGameProfile().getProperties().removeAll("textures");
        playerEntity.getGameProfile().getProperties().put("textures", skin);
    }
}
