/*
 * Copyright (c) 2017, Tyler Bucher
 * Copyright (c) 2017, Orion Stanger
 * Copyright (c) 2019, (Contributors)
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.edgegamers.picklez.Commands;

import lombok.NonNull;
import net.cpas.Cpas;
import net.cpas.model.InfoModel;
import net.cpas.model.SuccessResponseModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Main.Instance;
import org.edgegamers.picklez.Main.MinecraftCpas;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;

public class BanCommand extends SimpleCommand {

    private MinecraftCpas instance;

    public BanCommand() {
        super("ban");
        setMinArguments(3);
        setDescription("Command used to ban players using MAUL integration");
        setUsage("/ban <player> <time> <reason>");
        setPermission("cpas.ban");


        Instance instanceClass = new Instance();
        this.instance = instanceClass.getInstance();
    }

    @Override
    public void onCommand() {
        Player banner = getPlayer();
        OfflinePlayer bannedOffline = Bukkit.getOfflinePlayer(args[0]);

        String reason = "";

        for(int i = 2; i < args.length; i++) {
            reason += "" + args[i] + " ";
        }

        String[] admins = getAdminList();

        int duration = Integer.parseInt(args[1]);

        if(checkBanRules(banner, bannedOffline.getPlayer())) {
            Cpas.getInstance().banUser(
                    bannedOffline.getUniqueId().toString(),
                    bannedOffline.getName(),
                    banner.getUniqueId().toString(),
                    admins,
                    duration,
                    reason,
                    new ProcessBanResponse(instance, banner, bannedOffline, reason, duration)
            );

            Common.tell(banner, "&cMAUL &8\u00BB &7Player has successfully been banned");
        }

        else {
            Common.tell(banner, "&cMAUL &8\u00BB &7That ban didn't go through! You either can't ban that user, or MAUL is not initialized correctly");
        }

        String kickMessage = Common.colorize("&cYou were banned from this server!\n\n&cBanned by: &7" + banner.getName() + "\n&cDuration: &7" + duration + " &7minute(s)" + "\n&cReason: &7&o" + reason + "\n\n&7Contest at: &f&nhttps://edge-gamers.com");
        if(bannedOffline.getPlayer().isOnline()) {
            bannedOffline.getPlayer().kickPlayer(kickMessage);
        }



    }
    private String[] getAdminList() {
        int current = 0;
        String[] admins = new String[10];
        Arrays.fill(admins, "");
        for(InfoModel infoModel : instance.getAdminPlayerCache()) {
            if(current < 10) {
                admins[current++] = infoModel.gameId.toString();
            }
            else {
                break;
            }
        }
        return admins;
    }

    private boolean checkBanRules(Player admin, Player player) {
        InfoModel adminInfoModel = null;
        InfoModel playerInfoModel = null;

        for(InfoModel infoModel : instance.getAdminPlayerCache()) {
            if(infoModel.gameId.equals(admin.getUniqueId())) {
                adminInfoModel = infoModel;
            }
            if(infoModel.gameId.equals(player.getUniqueId())) {
                playerInfoModel = infoModel;
            }
        }
        if(playerInfoModel == null) {
            return true;
        }
        else if(adminInfoModel == null) {
            return false;
        }
        final int cutOff = instance.retrieveConfig().getBanRankThreshold();
        if(playerInfoModel.primaryGroup.rank >= cutOff && adminInfoModel.primaryGroup.rank < cutOff) {
            return false;
        }
        return true;
    }

    private static class ProcessBanResponse implements Cpas.ProcessResponse<SuccessResponseModel> {
        private final MinecraftCpas instance;
        private final Player admin;
        private final OfflinePlayer player;
        private final String reason;
        private final int duration;

        ProcessBanResponse(@NonNull MinecraftCpas instance, @NonNull Player admin, @NonNull OfflinePlayer player, @NonNull String reason, int duration) {
            this.instance = instance;
            this.admin = admin;
            this.player = player;
            this.reason = reason;
            this.duration = duration;
        }

        @Override
        public void process(SuccessResponseModel successResponseModel, String errorResponse) {
            if(errorResponse != null || !successResponseModel.success) {
                instance.getLogger().warning("Ban was not executed correctly! Admin: " + admin.getDisplayName() + " | Banned: " + player.getName());
                Common.tell(admin, "&cMAUL &8\u00BB &7That ban didn't go through, please contact Tech to fix it. In the meantime, use a local ban (essentials) on the hub.");
                Common.broadcast(errorResponse);
            }


        }

        @Override
        public Class<SuccessResponseModel> getModelClass() {
            return SuccessResponseModel.class;
        }

    }

}
