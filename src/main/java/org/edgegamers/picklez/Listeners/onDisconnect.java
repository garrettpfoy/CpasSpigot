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
package org.edgegamers.picklez.Listeners;

import net.cpas.model.InfoModel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.edgegamers.picklez.Main.Instance;
import org.edgegamers.picklez.Main.MinecraftCpas;
import org.edgegamers.picklez.Storage.CacheDatabase;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.edgegamers.picklez.Storage.PlayerData;
import org.mineacademy.fo.Common;

public class onDisconnect implements Listener {

    private MinecraftCpas instance;

    public onDisconnect() {
        Instance temp = new Instance();
        this.instance = temp.getInstance();
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        final Player player = event.getPlayer();


        CpasPlayerCache cache = CpasPlayerCache.getCache(player.getUniqueId());

        Common.runLaterAsync(() -> CacheDatabase.getInstance().save(event.getPlayer().getName(), player.getUniqueId(), cache));


        if(player.isOnline()) {
            final InfoModel adminInfoModel = instance.getPlayerInfoModel(player.getUniqueId());
            if(adminInfoModel != null) {
                instance.getAdminPlayerCache().remove(adminInfoModel);
            }
        }
        if(!(player.hasPermission("cpas.silent"))) {
            String coloredMessage = Common.colorize("&c&l(&c-&c&l) " + getRankFormatted(event.getPlayer()) + "&6" + event.getPlayer().getDisplayName());
            event.setQuitMessage(coloredMessage);
        }
        else {
            event.setQuitMessage(null);
        }
    }

    private static String getRankFormatted(Player player) {
        PlayerData data = new PlayerData(player.getUniqueId().toString());
        int rank = data.getRank();

        if (rank == 10) {
            return Common.colorize("&7=(&be&7)= &6");
        } else if (rank == 20) {
            return Common.colorize("&7=(&3eG&7)= &6");
        } else if (rank == 30) {
            return Common.colorize("&7=(&9eGO&7)= &6");
        } else if (rank == 60) {
            return Common.colorize("&7[&aManager&7] &6");
        } else if (rank == 70) {
            return Common.colorize("&7[&2SrMgr&7] &6");
        } else if (rank == 90) {
            return Common.colorize("&7[&dComMgr&7] &6");
        } else if (rank == 91) {
            return Common.colorize("&7[&4Director&7] &6");
        } else {
            return "";
        }
    }

}
