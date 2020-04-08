package org.edgegamers.picklez.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Storage.PlayerData;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;
import java.util.List;

public class NickCommand extends SimpleCommand {

    public NickCommand() {
        super("nick");
        setPermission("cpas.nick");
        setUsage("/nick <?player> <nickName>");
        setAutoHandleHelp(true);
    }

    @Override
    public void onCommand() {
        Player user = getPlayer();

        if(args.length > 1) {
            //Modify another player's nickname
            if(!(user.hasPermission("cpas.nick.others"))) {
                Common.tell(user, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou can't nickname other players!");
                return;
            }
            if(args[0].equalsIgnoreCase(user.getName())) {
                Common.tell(user, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bPlease do not set your name via this method, simply use &f/nick [nickname] &bto set your own nickname.");
            }

            String playerName = args[0];
            String nickTemp = args[1];

            if(Bukkit.getPlayer(playerName).hasPermission("cpas.nick.others") && !(user.hasPermission("cpas.nick.bypass"))) {
                Common.tell(user, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou can not nickname that player!");
                return;
            }

            //Now we must take the nickname, and make sure it matches their forum name
            //to do this, we must first remove all &[letter] as we don't want to compare a colorized nickname
            String nick = Common.colorize(nickTemp);
            String nickCheck = ChatColor.stripColor(nick);
            //Now we have the nickname without any color codes, lets check them to their data on file
            //to see if it matches their forum name
            PlayerData data = new PlayerData(Bukkit.getPlayer(playerName).getUniqueId().toString());

            if(!(data.getForumName().equalsIgnoreCase(nickCheck)) && !(user.hasPermission("cpas.nick.bypass"))) {
                //Runs if not equal && player does not have bypass permission
                Common.tell(user, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat name does not match that player's forum name! Forum Name: &f" + data.getForumName());
                return;
            }

            nick = "~" + nick;

            Bukkit.getPlayer(playerName).setPlayerListName(nick);
            Bukkit.getPlayer(playerName).setDisplayName(nick);
            Bukkit.getPlayer(playerName).setCustomName(nick);
            Bukkit.getPlayer(playerName).setCustomNameVisible(true);

            data.setNickname(nick);
            data.setSearch(playerName + " | " + nick);

            Common.tell(Bukkit.getPlayer(playerName), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bPlayer's nickname has been changed.");
            Common.tell(user, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYour nickname has been changed.");
        }
        else {
            //Modify one's own nickname
            if(!(getPlayer().hasPermission("cpas.nick.self"))) {
                Common.tell(user, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou do not have permission to set your own nickname!");
                return;
            }
            String playerName = user.getName();
            String nickTemp = args[0];

            String nick = Common.colorize(nickTemp);
            String nickCheck = ChatColor.stripColor(nick);

            PlayerData data = new PlayerData(user.getUniqueId().toString());

            if(!(data.getForumName().equalsIgnoreCase(nickCheck)) && !(user.hasPermission("cpas.nick.bypass"))) {
                //Runs if not equal && player does not have bypass permission
                Common.tell(user, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat name does not match that player's forum name! Forum Name: &f" + data.getForumName());
                return;
            }

            nick = "~" + nick;

            Bukkit.getPlayer(playerName).setPlayerListName(nick);
            Bukkit.getPlayer(playerName).setDisplayName(nick);
            Bukkit.getPlayer(playerName).setCustomName(nick);
            Bukkit.getPlayer(playerName).setCustomNameVisible(true);

            data.setNickname(nick);
            data.setSearch(playerName + " | " + nick);

            Common.tell(Bukkit.getPlayer(playerName), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYour nickname has been changed.");
        }

    }
}
