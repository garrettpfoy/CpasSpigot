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
import net.cpas.model.CpasGroupModel;
import net.cpas.model.InfoModel;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.edgegamers.picklez.Storage.CacheDatabase;
import org.edgegamers.picklez.Storage.Config;
import org.edgegamers.picklez.Main.MinecraftCpas;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;

import javax.naming.Context;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class onLogin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
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
        final InetAddress playerAddress = Objects.requireNonNull(event.getPlayer().getAddress()).getAddress();

        event.setJoinMessage(null);

        final CpasPlayerCache cache = CpasPlayerCache.getCache(event.getPlayer().getUniqueId());

        CacheDatabase.getInstance().load(event.getPlayer().getUniqueId(), cache);


        /*
        Runs everytime a player logs in, and is technically online.
         */

        Cpas.getInstance().getInfo(playerUUID.toString(), playerAddress.getHostAddress(), false, new ProcessInfoModelResponse((MinecraftCpas) MinecraftCpas.getInstance(), playerUUID, event.getPlayer(), true, event));

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

        @Override
        public void process(InfoModel response, String errorMessage) {

            /*
            As of version 3.0 we no longer use PlayerData and have moved over to PlayerCache
             */

            //Now that we have the player cache, lets set everything to what we want it to be set as!

            CpasPlayerCache playerData = CpasPlayerCache.getCache(playerUUID);
            playerData.setUsername(player.getName());
            playerData.setForumName(response.forumName);
            playerData.setRankName(response.primaryGroup.name);
            playerData.setDivisionName(response.divisionName);
            playerData.setVerificationExpired(response.verificationExpired);
            playerData.setDedicatedSupporter(response.dsInfo.isDedicatedSupporter);
            playerData.setRank(response.primaryGroup.rank);

            //All right, well all the *easy* data has been set, lets go through the harder ones.
            List<String> groupsTemp = new ArrayList<>();

            for(CpasGroupModel group : response.groups) {
                groupsTemp.add(group.name);
            }

            playerData.setGroups(groupsTemp);

            if(response.primaryGroup.rank > 0) {
                playerData.setIsMember(true);
                playerData.setIsRegistered(true);
            }
            else if(response.forumName == null || response.forumName.length() <= 0) {
                //Player is a pub
                playerData.setIsMember(false);
                playerData.setIsRegistered(false);
                event.setJoinMessage(null);
                player.setDisplayName(player.getName());
                Common.broadcast("&a&l(+) &6" + player.getDisplayName());
                return;
            }
            else {
                //Player probably has a rank of 0, so pub but not a member, PROBABLY?!
                playerData.setIsRegistered(true);
                playerData.setIsMember(false);
            }


            final CpasGroupModel atLeastAdminGroup = pluginInstance.retrieveConfig().getAtLeastAdminGroup();
            //remove admin from list just in case they are in it
            final InfoModel adminInfoModel = pluginInstance.getPlayerInfoModel(playerUUID);
            if (adminInfoModel != null) {
                pluginInstance.getAdminPlayerCache().remove(adminInfoModel);
            }
            //adds an admin to the list
            if (checkContainingGroups(response.groups, atLeastAdminGroup)) {
                pluginInstance.getAdminPlayerCache().add(response);
            }

            /*
            LuckPerms is currently one of the only plugins that is lightweight, and strong enough to handle
            a global network, and thus I am confident enough to strong-depend on it for the near future. Thus,
            We will be using LuckPerms in CPAS to hook into
             */

            final LuckPerms permission = MinecraftCpas.getPerms();

            /*
            In theory this should be getOfflinePlayer, but due to the deprecation and the lack of support it
            provides currently I have opted to keep it as a normal player.
             */
            final Player player = Bukkit.getPlayer(playerUUID);


            /*
            Here we set their name to their forum name if they are not DS
            and if they aren't Leadership or above
             */

            //If they are leadership, we don't touch them
            if (response.primaryGroup.rank >= 60) {
                //runs if leadership
                Bukkit.getPlayer(playerUUID).setDisplayName(playerData.getNickName());
                Bukkit.getPlayer(playerUUID).setCustomName(playerData.getNickName());
                Bukkit.getPlayer(playerUUID).setCustomNameVisible(true);
            }
            /*
            Here we handle DS, originally I had a check to see whether or not it matches their forum name, but this is silly as
            it doesn't take into account Leadership setting the forum name for whatever reason. Only way to get a nickname that ISN'T
            your forum name is via leadership command, so no need to check it here.
             */
            else if (response.dsInfo.isDedicatedSupporter) {
                    Bukkit.getPlayer(playerUUID).setDisplayName(playerData.getNickName());
                    Bukkit.getPlayer(playerUUID).setCustomName(playerData.getNickName());
                    Bukkit.getPlayer(playerUUID).setCustomNameVisible(true);
            }
            //If they are not leadership, nor are they DS, we just set display name to be
            //their forum name, no questions asked :)
            else {
                Bukkit.getPlayer(playerUUID).setDisplayName(response.forumName);
                Bukkit.getPlayer(playerUUID).setCustomName(response.forumName);
                Bukkit.getPlayer(playerUUID).setCustomNameVisible(true);
            }


            /*
            Here I go through the player's groups, and if that group is defined in pretty much any
            of the config role lists, I remove them.
             */
            Set<String> groups = permission.getUserManager().getUser(playerUUID).getNodes().stream()
                    .filter(NodeType.INHERITANCE::matches)
                    .map(NodeType.INHERITANCE::cast)
                    .map(InheritanceNode::getGroupName)
                    .collect(Collectors.toSet());


            final Config config = pluginInstance.retrieveConfig();

            for (String group : groups) {
                if (checkIfRole(group, config)) {
                    try {
                        removeGroupFromPlayer(group, Bukkit.getPlayer(playerUUID), true);
                    } catch (InterruptedException | TimeoutException | ExecutionException e) {
                        Common.broadcast("&c&lERROR &8\u00BB &7" + e.getMessage());
                    }
                }
            }

            /*
            Default group normally should never be able to be taken away, however sometimes hooking into plugin
            API's will result in unseen obstructions, so keeping this in to maintain the plugin's core features
             */

            try {
                addGroupToPlayer("default", Bukkit.getPlayer(playerUUID), true);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                Common.broadcast("&c&lERROR &8\u00BB &7" + e.getMessage());
            }

            /*
            Here we get the primary rank from CPAS, and if the config has that
            primary group as a key, we then get the value at that key, and set
            the player's group to that group.

            The primary group function will be defined in the LuckPerms
            or whatever permission handler the server will use. This plugin will NOT
            handle that as it most likely will cause conflict.
             */

            final SerializedMap primaryMap = config.getPrimaryGroups();
            int max = 0;
            for (String rank : primaryMap.keySet()) {
                if (isMemberOfGroup(response, Integer.parseInt(rank))) {
                    if (max < Integer.parseInt(rank)) {
                        max = Integer.parseInt(rank);
                    }
                }
            }
            try {
                addGroupToPlayer(primaryMap.getString(String.valueOf(max)), Bukkit.getPlayer(playerUUID), true);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                Common.broadcast("&c&lERROR &8\u00BB &7" + e.getMessage());
            }




            /*
            Here we give back the dedicated supporter rank (set in the config)
            to a player if CPAS tells us they are a dedicated supporter.

            ONLY IF it is enabled in Config
             */

            if (pluginInstance.retrieveConfig().isUseDsGroup() && login) {
                if (response.dsInfo.isDedicatedSupporter) {
                    try {
                        addGroupToPlayer("DS", Bukkit.getPlayer(playerUUID), true);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        Common.broadcast("&c&lERROR &8\u00BB &7" + e.getMessage());
                    }

                    if (response.dsInfo.isDedicatedSupporter && response.dsInfo.joinMessage.length() != 0 && !(player.hasPermission("cpas.silent"))) {
                        String message = response.dsInfo.joinMessage;
                        //Leadership instructed me to remove all ugly af colors from DS join messages
                        message = message.replaceAll("&0", "");
                        message = message.replaceAll("&l", "");
                        message = message.replaceAll("&m", "");
                        message = message.replaceAll("&n", "");
                        message = message.replaceAll("&1", "");
                        message = message.replaceAll("&k", "");


                        Common.broadcast("&a&l(&a+&a&l) &6" + getRankFormatted(player) + "&6" + player.getDisplayName() + " &8- &7" + message);
                    } else if (player.hasPermission("cpas.silent")) {
                        Common.tell(player, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou have logged in silently!");
                    } else {
                        Common.broadcast("&a&l(&a+&a&l) &6" + getRankFormatted(player) + player.getDisplayName());
                    }
                } else {
                    try {
                        removeGroupFromPlayer("DS", Bukkit.getPlayer(playerUUID), true);
                    } catch (InterruptedException | TimeoutException | ExecutionException e) {
                        Common.broadcast("&c&lERROR &8\u00BB &7" + e.getMessage());
                    }
                    Common.broadcast("&a&l(&a+&a&l) &6" + getRankFormatted(player) + player.getDisplayName());
                }
            }

            /*
            Here we get the map of the secondary groups, and if
            the key is defined for the integer of the rank
            we give the role associated with that
            key.
             */

            final SerializedMap secondaryMap = config.getSecondaryGroups();
            for (String rank : secondaryMap.keySet()) {
                if (isMemberOfGroup(response, Integer.parseInt(rank))) {
                    try {
                        addGroupToPlayer(secondaryMap.getString(rank), Bukkit.getPlayer(playerUUID), true);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        Common.broadcast("&c&lERROR &8\u00BB &7" + e.getMessage());
                    }
                }
            }

            /*
            Here we get the map of the division groups, and if
            a key is defined for the integer of the rank,
            we give the role associated with that key.
             */

            final SerializedMap divisionMap = config.getDivisionGroups();
            for (String rank : divisionMap.keySet()) {
                if (isMemberOfGroup(response, rank)) {
                    try {
                        addGroupToPlayer(divisionMap.getString(rank), Bukkit.getPlayer(playerUUID), true);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        Common.broadcast("&c&lERROR &8\u00BB &7" + e.getMessage());
                    }
                }
            }
        }

        @Override
        public Class<InfoModel> getModelClass() {
            return InfoModel.class;
        }

        private boolean checkContainingGroups(List<CpasGroupModel> groups, CpasGroupModel group) {

            /*
            This goes through all groups CPAS gives us, and checks with the group given to see if they are a match
            if they are, it returns true.
             */
            for (CpasGroupModel cpasGroupModel : groups) {
                if (cpasGroupModel.rank == group.rank && cpasGroupModel.name.equals(group.name)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isMemberOfGroup(InfoModel response, int rank) {
            for (CpasGroupModel group : response.groups) {
                if (group.rank == rank) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isMemberOfGroup(InfoModel response, String rank) {
            //Checks if the player is a m
            if(response.division.equalsIgnoreCase(rank)) {
                return true;
            }
            return false;
        }

        public static boolean checkIfRole(String group, Config config) {

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
            for (String key : primaryKeys) {
                if (config.getPrimaryGroups().getString(key).equalsIgnoreCase(group)) {
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
            for (String key : secondaryKeys) {
                if (config.getSecondaryGroups().getString(key).equalsIgnoreCase(group)) {
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
            for (String key : divisionKeys) {
                if (config.getDivisionGroups().getString(key).equalsIgnoreCase(group)) {
                    return true;
                }
            }

            /*
            If all those checks fail, we will return false assuming we checked all of the ones they
            wanted removed
             */
            return false;
        }


        private static String getRankFormatted(Player player) {
            CpasPlayerCache data = CpasPlayerCache.getCache(player.getUniqueId());
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

        private static boolean addGroupToPlayer(String groupName, OfflinePlayer player, boolean isGlobal) throws InterruptedException, ExecutionException, TimeoutException {

            LuckPerms permissions = MinecraftCpas.getPerms();

            Group group = permissions.getGroupManager().getGroup(groupName);


            if (group != null) {
                CompletableFuture<User> userFuture = permissions.getUserManager().loadUser(player.getUniqueId());

                userFuture.thenAcceptAsync(user -> {
                    if (isGlobal) {
                        InheritanceNode node = InheritanceNode.builder(groupName).withContext(DefaultContextKeys.SERVER_KEY, "global").value(true).build();
                        user.data().add(node);

                        permissions.getUserManager().saveUser(user);
                    } else {
                        InheritanceNode node = InheritanceNode.builder(groupName).value(true).build();
                        user.data().add(node);
                        permissions.getUserManager().saveUser(user);
                    }
                }).get(10, TimeUnit.SECONDS);

                return true;
            } else {
                return false;
            }
        }

        private static boolean removeGroupFromPlayer(String groupName, Player player, boolean isGlobal) throws InterruptedException, ExecutionException, TimeoutException {

            LuckPerms permissions = MinecraftCpas.getPerms();

            Group group = permissions.getGroupManager().getGroup(groupName);

            if (group != null) {
                CompletableFuture<User> userFuture = permissions.getUserManager().loadUser(player.getUniqueId());

                userFuture.thenAcceptAsync(user -> {
                    if (isGlobal) {
                        InheritanceNode node = InheritanceNode.builder(groupName).withContext(DefaultContextKeys.SERVER_KEY, "global").value(true).build();
                        user.data().remove(node);
                        permissions.getUserManager().saveUser(user);
                    } else {
                        InheritanceNode node = InheritanceNode.builder(groupName).value(true).build();
                        user.data().remove(node);
                        permissions.getUserManager().saveUser(user);
                    }
                }).get(10, TimeUnit.SECONDS);

                return true;
            } else {
                return false;
            }
        }
    }
}