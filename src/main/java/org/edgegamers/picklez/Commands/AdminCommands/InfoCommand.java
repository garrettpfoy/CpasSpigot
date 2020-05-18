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
import net.cpas.model.InfoModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Main.Instance;
import org.edgegamers.picklez.Main.MinecraftCpas;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

public class InfoCommand extends SimpleCommand {

    private MinecraftCpas instance;

    public InfoCommand() {
        super("playerinfo");
        setPermission("cpas.info");
        setMinArguments(1);
        setDescription("Gets a player's information");
        setUsage("/info [player]");
        setAutoHandleHelp(true);

        Instance temp = new Instance();
        instance = temp.getInstance();
    }

    @Override
    public void onCommand() {
        Player sender = getPlayer();
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        Cpas.getInstance().getInfo(target.getUniqueId().toString(), false, new ProcessInfoResponse(instance, sender, target));
    }

    private static class ProcessInfoResponse implements Cpas.ProcessResponse<InfoModel> {

        private final MinecraftCpas instance;
        private final Player sender;
        private final OfflinePlayer target;

        ProcessInfoResponse(@NonNull MinecraftCpas instance, @NonNull Player sender, @NonNull OfflinePlayer target) {
            this.instance = instance;
            this.sender = sender;
            this.target = target;
        }

        @Override
        public void process(InfoModel infoModel, String errorResponse) {
            if(errorResponse != null) {
                instance.getLogger().warning("Oh no! User information could not be generated!");
                Common.broadcast(errorResponse);
                return;
            }

            Common.tell(sender, "&8&l---------------[ &9" + "Player" + " &7Info &8&l]---------------");
            if(infoModel.userId > 0) {
                Common.tell(sender, "&7");
                Common.tell(sender, "&3Forum Name: &7" + infoModel.forumName);
                Common.tell(sender, "&3Rank: &7" + infoModel.primaryGroup.name);
                Common.tell(sender, "&3Division: &7" + infoModel.division);
                Common.tell(sender, "&3Dedicated Supporter: &7" + infoModel.dsInfo.isDedicatedSupporter);
                Common.tell(sender, "&3UUID: &7" + target.getUniqueId().toString());
                Common.tell(sender, "&7");
                Common.tell(sender, "&8&l----------------------------------------");
            }
            else {
                Common.tell(sender, "&7");
                Common.tell(sender, "&3Forum Name: &7N/A");
                Common.tell(sender, "&3Rank: &7N/A");
                Common.tell(sender, "&3Division: &7N/A");
                Common.tell(sender, "&3Dedicated Supporter: &7" + infoModel.dsInfo.isDedicatedSupporter);
                Common.tell(sender, "&3UUID: &7" + target.getUniqueId().toString());
                Common.tell(sender, "&3");
                Common.tell(sender, "&8&l----------------------------------------");
            }
        }

        @Override
        public Class<InfoModel> getModelClass() {
            return InfoModel.class;
        }

    }

}
