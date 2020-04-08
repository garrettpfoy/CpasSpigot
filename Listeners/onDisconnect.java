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

        if(player.isOnline()) {
            final InfoModel adminInfoModel = instance.getPlayerInfoModel(player.getUniqueId());
            if(adminInfoModel != null) {
                instance.getAdminPlayerCache().remove(adminInfoModel);
            }
        }
        if(!(player.hasPermission("cpas.silent"))) {
            String coloredMessage = Common.colorize("&c&l(&c-&c&l) &6" + event.getPlayer().getDisplayName());
            event.setQuitMessage(coloredMessage);
        }
        else {
            event.setQuitMessage(null);
        }
    }

}
