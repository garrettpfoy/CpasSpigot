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
package org.edgegamers.picklez.Commands.AdminCommands;

import lombok.NonNull;
import net.cpas.Cpas;
import net.cpas.model.BanHistoryModel;
import net.cpas.model.CpasBanModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Main.Instance;
import org.edgegamers.picklez.Main.MinecraftCpas;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BanHistoryCommand extends SimpleCommand {

    private MinecraftCpas instance;

    public BanHistoryCommand() {
        super("history");
        setMinArguments(1);
        setDescription("MAUL integreted ban history information");
        setUsage("/history <playerName> <entries>");
        setAutoHandleHelp(true);
        setPermission("cpas.history");

        Instance temp = new Instance();
        instance = temp.getInstance();
    }


    @Override
    public void onCommand() {
        String playerName = args[0];
        int entries = 100;
        if(args.length == 2) {
            entries = Integer.parseInt(args[1]);
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

        Cpas.getInstance().getBanHistory(
                player.getUniqueId().toString(),
                entries,
                new ProcessBanHistoryResponse(instance, getPlayer(), player)
        );

    }

    public static class ProcessBanHistoryResponse implements Cpas.ProcessResponse<BanHistoryModel> {
        private MinecraftCpas instance;
        private Player admin;
        private OfflinePlayer player;

        ProcessBanHistoryResponse(@NonNull MinecraftCpas instance, @NonNull Player admin, @NonNull OfflinePlayer player) {
            this.instance = instance;
            this.admin = admin;
            this.player = player;
        }

        @Override
        public void process(BanHistoryModel banHistoryModel, String errorResponse) {
            if(errorResponse != null) {
                instance.getLogger().warning("Requesting ban history command failed! See errors.txt for more details");
                Common.tell(admin, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &r&bError! There was a problem with fetching that player's ban history... please contact tech!");
                return;
            }

            if(banHistoryModel.bans.isEmpty()) {
                Common.tell(admin, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &r&bThere is no ban history for that player");
                return;
            }

            Common.tell(admin, "&8&l--------------[&r &c&lMAUL &7History &8&l]--------------");
            Common.tell(admin, "&7");

            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            int count = 0;
            for(CpasBanModel ban : banHistoryModel.bans) {
                final Date date = new Date(ban.banDateSeconds * 1000L);
                final String formattedDate = formatter.format(date);

                if(ban.duration > 0) {
                    Common.tell(admin, "&8[" +  formattedDate + "] " + "&c" + player.getName() + "&7 was banned for &c" + ban.length + "&7 minutes. There are: &c" + ban.duration + "&7minutes remaining.");
                    Common.tell(admin, "&cReason: &7&o" + ban.reason);
                    Common.tell(admin, "&7");
                }
                else if(ban.duration < 0) {
                    Common.tell(admin, "&8[" +  formattedDate + "] " + "&c" + player.getName() + "&7was &c&opermanently &7banned.");
                    Common.tell(admin, "&cReason: &7&o" + ban.reason);
                    Common.tell(admin, "&7");
                }
                else if(ban.duration == 0) {
                    Common.tell(admin, "&8[" +  formattedDate + "] " + "&c" + player.getName() + " &7was banned for &c" + ban.length + "&7 minutes.");
                    Common.tell(admin, "&cReason: &7&o" + ban.reason);
                    Common.tell(admin, "&7");
                }
            }
            Common.tell(admin, "&8&l----------------------------------------");
            Common.tell(admin, "&cTotal Bans: &7" + banHistoryModel.bans.size());

        }

        @Override
        public Class<BanHistoryModel> getModelClass() {
            return BanHistoryModel.class;
        }



    }

}
