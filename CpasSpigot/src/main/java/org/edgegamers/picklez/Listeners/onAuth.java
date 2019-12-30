package org.edgegamers.picklez.Listeners;

import fr.xephi.authme.events.LoginEvent;
import lombok.NonNull;
import net.cpas.Cpas;
import net.cpas.model.BanInfoModel;
import net.cpas.model.CpasGroupModel;
import net.cpas.model.InfoModel;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.edgegamers.picklez.Main.Instance;
import org.edgegamers.picklez.Main.MinecraftCpas;
import org.edgegamers.picklez.Storage.Config;
import org.mineacademy.fo.Common;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class onAuth implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) throws ExecutionException, InterruptedException {
        /*
        This is run everytime a player logs in (obviously), here I will pretty much
        get check if the player is online (which should almost always occur), and then pretty much handles all the
        info CPAS gives the plugin (from the response). Things I accomplish here are:
        - Takes away all roles (that are a role in the config)
        - Gives primary role (mod, admin, leadership, etc)
        - Gives division role (mc, media, etc)
        - Gives secondary role if applicable (Event coordinator, recruitment coordinator, etc)
        - Gives DS role if they have it
         */
        final UUID playerUUID = event.getUniqueId();
        Instance instance = new Instance();


        /*
        Runs everytime a player logs in, and is technically online.
         */

        Cpas.getInstance().getBanInfo(playerUUID.toString(), new ProcessBanInfoResponse(instance.getInstance(), event)).get();
    }


    public static class ProcessBanInfoResponse implements Cpas.ProcessResponse<BanInfoModel> {
        private final MinecraftCpas pluginInstance;
        private final AsyncPlayerPreLoginEvent event;


        public ProcessBanInfoResponse(@NonNull MinecraftCpas pluginInstance, AsyncPlayerPreLoginEvent event) {
            this.pluginInstance = pluginInstance;
            this.event = event;
        }

        @Override
        public void process(BanInfoModel response, String errorMessage) {
            if (errorMessage == null && response.duration == -1) {
                pluginInstance.getLogger().warning("Player is banned... player has been blocked from logging in.");
                String permMessage = Common.colorize("&cYou are currently banned from this server\n\n&cDuration: &7Permanent\n&cReason: &7" + response.reason + "\n\n&7Contest at: &f&nhttps://edge-gamers.com/");
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, permMessage);
                return;
            } else if (response.duration > 0) {
                pluginInstance.getLogger().warning("Player is banned... player has been blocked from logging in.");
                String tempMessage = Common.colorize("&cYou are currently banned from this server\n\n&cDuration: &7" + response.duration + " minute(s)" + "\n&cReason: &7" + response.reason + "\n\n&7Contest at: &f&nhttps://edge-gamers.com/");
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, tempMessage);
                return;
            }
            pluginInstance.getLogger().info("Player is not banned, and has been allowed to log in.");
            return;

        }

        @Override
        public Class<BanInfoModel> getModelClass() {
            return BanInfoModel.class;
        }
    }


}