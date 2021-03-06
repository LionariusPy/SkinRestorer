package ru.lionarius.skinrestorer.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import ru.lionarius.skinrestorer.SkinRestorer;
import ru.lionarius.skinrestorer.util.WebUtils;

import java.util.Collection;
import java.util.Collections;

import static net.minecraft.command.argument.GameProfileArgumentType.getProfileArgument;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SkinCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("skin")
                .then(argument("skin_owner", StringArgumentType.word())
                        .executes(context -> setSkin(context.getSource(), Collections.singleton(context.getSource().getPlayer().getGameProfile()),
                                StringArgumentType.getString(context, "skin_owner"), false))
                        .then(argument("player", new GameProfileArgumentType()).requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                                .executes(context -> setSkin(context.getSource(), getProfileArgument(context, "player"),
                                        StringArgumentType.getString(context, "skin_owner"), true))))
        );
    }

    private static int setSkin(ServerCommandSource source, Collection<GameProfile> players, String skin_owner, boolean flag) {
        players.forEach(player -> {
            ServerPlayerEntity playerEntity = source.getMinecraftServer().getPlayerManager().getPlayer(player.getId());

            if(playerEntity == null) {
                try {
                    source.getPlayer().sendMessage(
                            new LiteralText("§a[SkinRestorer]§f Specified player doesn't exist."), false);
                    return;
                } catch (Exception e) { return; }
            }

            try {
                SkinRestorer.configManager.updatePlayerSkin(player.getId(), WebUtils.getSkin(WebUtils.getUUID(skin_owner)));

                if (!flag)
                    playerEntity.sendMessage(
                            new LiteralText("§a[SkinRestorer]§f You need to reconnect to apply skin."), false);
                else
                    playerEntity.sendMessage(
                            new LiteralText("§a[SkinRestorer]§f Operator changed your skin. You need to reconnect to apply it."), false);

            } catch (Exception e) {
                playerEntity.sendMessage(
                        new LiteralText("§a[SkinRestorer]§f Couldn't find skin with specified name."), false);
            }
        });

        return 0;
    }
}
