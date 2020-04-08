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
package org.edgegamers.picklez.Main;

import net.cpas.model.InfoModel;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.edgegamers.picklez.Commands.*;
import org.edgegamers.picklez.Listeners.onAuth;
import org.edgegamers.picklez.Storage.Config;
import org.edgegamers.picklez.Listeners.onDisconnect;
import org.edgegamers.picklez.Listeners.onLogin;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.TreeSet;
import java.util.UUID;

public class MinecraftCpas extends SimplePlugin {

    private Config config;
    private TreeSet<InfoModel> adminPlayerCache;
    private LuckPerms api;
    private MinecraftCpas main;

    @Override
    public void onPluginStart() {
        getLogger().info("Attempting to load MinecraftCPAS...");
        main = this;
        Instance instance = new Instance();
        instance.setInstance(this);

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }

        getLogger().info("Loading configuration files...");
        //Loads new config
        config = new Config();
        getLogger().info("Done!");

        getLogger().info("Configuring CPAS...");
        //loads CPAS/configs CPAS
        config.configCpas();
        getLogger().info("Done!");


        //Sets up admin player cache
        adminPlayerCache = new TreeSet<>((o1, o2)->o2.primaryGroup.rank - o1.primaryGroup.rank);

        getLogger().info("Attempting to load listeners...");
        //Load listeners here:
        registerEvents(new onDisconnect());
        registerEvents(new onLogin());
        registerEvents(new onAuth());
        getLogger().info("Done!");

        getLogger().info("Attempting to load commands...");
        //Load commands here:
        registerCommand(new BanCommand());
        registerCommand(new InfoCommand());
        registerCommand(new BanHistoryCommand());
        registerCommand(new NickCommand());
        registerCommand(new RealnameCommand());
        registerCommand(new UnnickCommand());
        registerCommand(new RealnickCommand());
        getLogger().info("Done!");

        getLogger().info("Plugin has been enabled!");
    }

    public Config retrieveConfig() {
        return config;
    }

    public InfoModel getPlayerInfoModel(UUID uuid) {
        for(InfoModel infoModel : this.getAdminPlayerCache()) {
            if(infoModel.gameId.equals(uuid)) {
                return infoModel;
            }
        }
        return null;
    }

    public MinecraftCpas getMain() {
        return main;
    }

    public TreeSet<InfoModel> getAdminPlayerCache() {
        return adminPlayerCache;
    }

    public LuckPerms getPerms() {
        return api;
    }
}
