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

import lombok.NonNull;
import net.cpas.Cpas;
import net.cpas.model.BanInfoModel;
import net.cpas.model.CpasGroupModel;
import net.cpas.model.InfoModel;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.edgegamers.picklez.Storage.Config;
import org.edgegamers.picklez.Main.Instance;
import org.edgegamers.picklez.Main.MinecraftCpas;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;

import javax.naming.Context;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class onLogin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
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

        final UUID playerUUID = event.getPlayer().getUniqueId();
        final InetAddress playerAddress = event.getPlayer().getAddress().getAddress();
        final Instance instance = new Instance();

        event.setJoinMessage(null);

        /*
        Runs everytime a player logs in, and is technically online.
         */
        Cpas.getInstance().getInfo(playerUUID.toString(), playerAddress.getHostAddress(), false, new ProcessInfoModelResponse(instance.getInstance(), playerUUID, event.getPlayer(), true, event));

    }


    public static class ProcessInfoModelResponse implements Cpas.ProcessResponse<InfoModel> {
        /*
        Initializing all params
         */
        private static final Set<Context> context = new HashSet<>();
        private final MinecraftCpas pluginInstance;
        private final UUID playerUUID;
        private final Player player;
        private final boolean login;
        private final PlayerJoinEvent event;

        ProcessInfoModelResponse(@NonNull MinecraftCpas pluginInstance, @NonNull UUID playerUUID, @NonNull Player player, boolean login, PlayerJoinEvent event) {
            this.pluginInstance = pluginInstance;
            this.playerUUID = playerUUID;
            this.player = player;
            this.login = login;
            this.event = event;
        }

        private boolean checkContainingGroups(List<CpasGroupModel> groups, CpasGroupModel group) {

            /*
            This goes through all groups CPAS gives us, and checks with the group given to see if they are a match
            if they are, it returns true.
             */
            for(CpasGroupModel cpasGroupModel : groups) {
                if(cpasGroupModel.rank == group.rank && cpasGroupModel.name.equals(group.name)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void process(InfoModel response, String errorMessage) {

            final CpasGroupModel atLeastAdminGroup = pluginInstance.retrieveConfig().getAtLeastAdminGroup();
            //remove admin from list just in case they are in it
            final InfoModel adminInfoModel = pluginInstance.getPlayerInfoModel(playerUUID);
            if(adminInfoModel != null) {
                pluginInstance.getAdminPlayerCache().remove(adminInfoModel);
            }
            //adds an admin to the list
            if(checkContainingGroups(response.groups, atLeastAdminGroup)) {
                pluginInstance.getAdminPlayerCache().add(response);
            }

            /*
            I played with the idea of hooking into LuckPerms specifically, however it was a bit
            too complicated for what I need done. Vault is one of the most downloaded (if not THE most downloaded)
            economy/permissions API plugins, so it would be rare to have a plugin that isn't using Vault.
             */

            final Permission permission = pluginInstance.getPerms();
            final Player player = Bukkit.getPlayer(playerUUID);

            /*
            Here we set their name to their forum name if they are not DS
            and if they aren't Leadership or above
             */

            if(response.primaryGroup.rank > 60 || response.dsInfo.isDedicatedSupporter) {
                Bukkit.getPlayer(playerUUID).setDisplayName(response.forumName);
                Bukkit.getPlayer(playerUUID).setCustomName(response.forumName);
                Bukkit.getPlayer(playerUUID).setCustomNameVisible(true);
            }

            /*
            Here I go through the player's groups, and if that group is defined in pretty much any
            of the config role lists, I remove them.
             */

            String[] groups = permission.getPlayerGroups(player);
            final Config config = pluginInstance.retrieveConfig();
            for(String group : groups) {
                if(checkIfRole(group, config)) {
                    //After the check, if the group exists in the config
                    //we remove it.
                    permission.playerRemoveGroup(player, group);
                }
            }

            /*
            In theory this really isn't needed unless you have LuckPerms installed.
            All players are default given default, and assuming you setup LuckPerms the
            common way most people will have most permissions set in the default group, so
            we want to keep that here.
             */

            permission.playerAddGroup(player, "default");

            /*
            Here we get the primary rank from CPAS, and if the config has that
            primary group as a key, we then get the value at that key, and set
            the player's group to that group.

            The primary group function will be defined in the LuckPerms
            or whatever permission handler the server will use. This plugin will NOT
            handle that as it most likely will cause conflict.
             */

            final SerializedMap primaryMap = config.getPrimaryGroups();
            for(String rank : primaryMap.keySet()) {
                if(isMemberOfGroup(response,Integer.parseInt(rank))) {
                    permission.playerAddGroup(player, primaryMap.getString(rank));
                }
            }

            /*
            Here we give back the dedicated supporter rank (set in the config)
            to a player if CPAS tells up they are a dedicated supporter.

            ONLY IF it is enabled in Config
             */

            if(pluginInstance.retrieveConfig().isUseDsGroup() && login) {
                if(response.dsInfo.isDedicatedSupporter) {
                    permission.playerAddGroup(player,config.getDsGroup());
                    Common.broadcast("&8&l(&a+&8&l) &7" + player.getDisplayName() + " &8- &7&o" + response.dsInfo.joinMessage);
                }
                else {
                    permission.playerRemoveGroup(player, config.getDsGroup());
                    Common.broadcast("&8&l(&a+&8&l) &7 " + player.getDisplayName());
                }
            }

            /*
            Here we get the map of the secondary groups, and if
            the key is defined for the integer of the rank
            we give the role associated with that
            key.
             */

            final SerializedMap secondaryMap = config.getSecondaryGroups();
            for(String rank : secondaryMap.keySet()) {
                if(isMemberOfGroup(response, Integer.parseInt(rank))) {
                    permission.playerAddGroup(player, secondaryMap.getString(rank));
                }
            }

            /*
            Here we get the map of the division groups, and if
            a key is defined for the integer of the rank,
            we give the role associated with that key.
             */

            final SerializedMap divisionMap = config.getDivisionGroups();
            for(String rank : divisionMap.keySet()) {
                if(isMemberOfGroup(response, Integer.parseInt(rank))) {
                    permission.playerAddGroup(player, divisionMap.getString(rank));
                }
            }

    }

    private boolean isMemberOfGroup(InfoModel response, int rank) {
        for(CpasGroupModel group : response.groups) {
            if(group.rank == rank) {
                return true;
            }
        }
        return false;
     }

    @Override
        public Class<InfoModel> getModelClass() {
            return InfoModel.class;
    }

    public boolean checkIfRole(String group, Config config) {
            /*
            This pretty much just takes a group a player has, and iterates through all defined roles
            in the config, and if they match, it returns true
             */

           /*
           This gets all the keys defined in PrimaryGroups in the config
           and iterates through them, finding the value associated with each.
           It then checks if that group is equal to the parameter group, and if so
           returns true.
            */
            Set<String> primaryKeys = config.getPrimaryGroups().keySet();
            for(String key : primaryKeys) {
                if(config.getPrimaryGroups().getString(key).equalsIgnoreCase(group)) {
                    return true;
                }
            }

            /*
           This gets all the keys defined in SecondaryGroups in the config
           and iterates through them, finding the value associated with each.
           It then checks if that group is equal to the parameter group, and if so
           returns true.
            */

            Set<String> secondaryKeys = config.getSecondaryGroups().keySet();
            for(String key : secondaryKeys) {
                if(config.getSecondaryGroups().getString(key).equalsIgnoreCase(group)) {
                    return true;
                }
            }

            /*
           This gets all the keys defined in DivisionGroups in the config
           and iterates through them, finding the value associated with each.
           It then checks if that group is equal to the parameter group, and if so
           returns true.
           */

            Set<String> divisionKeys = config.getDivisionGroups().keySet();
            for(String key : divisionKeys) {
                if(config.getDivisionGroups().getString(key).equalsIgnoreCase(group)) {
                    return true;
                }
            }

            /*
            If all those checks fail, we will return false assuming we checked all of the ones they
            wanted removed
             */

            return false;
    }
    }
}