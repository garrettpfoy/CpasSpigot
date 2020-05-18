package org.edgegamers.picklez.Commands.AdminCommands;

import org.bukkit.Bukkit;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class MuteHistoryCommand extends SimpleCommand {

    public MuteHistoryCommand() {
        super("mutehistory");
        setUsage("/muteHistory [Player] <Entries>");
        setPermission("cpas.mutehistory");
    }

    @Override
    public void onCommand() {

        if(args.length == 0) {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bImpoper syntax: &f/mutehistory [Player] <Entries>");
            return;
        }
        CpasPlayerCache storage = CpasPlayerCache.getCache(Bukkit.getOfflinePlayer(args[0]).getUniqueId());

        if(args.length == 1) {
            //No entries defined
            List<String> mutes = storage.getMutes();

            if(mutes.size() == 0) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bNo mutes were found for that player!");
                return;
            }

            Common.tell(getPlayer(), "&8&l&m------------[&r &c&lMUTE &7History &8&l&m]&8&l&m------------");
            Common.tell(getPlayer(), "&7");
            for(int i = 0; (i < 5) && (i < mutes.size()); i++) {
                String[] formatted = mutes.get(i).split("@");
                int referenceNumber = Integer.parseInt(formatted[0]);
                String date = formatted[1];
                int duration = Integer.parseInt(formatted[2]);
                String user = formatted[3];
                String isActive = formatted[4];
                String reason = formatted[5];
                Common.tell(getPlayer(), "&8[&7&o" + date + "&8]");
                Common.tell(getPlayer(), "&7(&c#" + referenceNumber + "&7) &c" + args[0] + "&7 was muted by: &4" + user);
                Common.tell(getPlayer(), "&7Active: &c" + isActive + "&8 | &7Duration: &c" + duration + "s");
                Common.tell(getPlayer(), "&7Reason: &c&o" + reason);
                Common.tell(getPlayer(), "&7");
            }
            Common.tell(getPlayer(), "&8&8&l&m------------&8&l&m------------&8&l&m------------");
        }

        else if(args.length == 2) {
            //No entries defined
            List<String> mutes = storage.getMutes();

            if(mutes.size() == 0) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bNo mutes were found for that player!");
                return;
            }

            Common.tell(getPlayer(), "&8&l&m------------[&r &c&lMUTE &7History &8&l&m]&8&l&m------------");
            Common.tell(getPlayer(), "&7");
            for(int i = 0; (i < Integer.parseInt(args[1])) && (i < mutes.size()); i++) {
                String[] formatted = mutes.get(i).split("@");
                int referenceNumber = Integer.parseInt(formatted[0]);
                String date = formatted[1];
                int duration = Integer.parseInt(formatted[2]);
                String user = formatted[3];
                String isActive = formatted[4];
                String reason = formatted[5];
                Common.tell(getPlayer(), "&8[&7&o" + date + "&8]");
                Common.tell(getPlayer(), "&7(&c#" + referenceNumber + "&7) &c" + args[0] + "&7 was muted by: &4" + user);
                Common.tell(getPlayer(), "&7Active: &c" + isActive + "&8 | &7Duration: &c" + duration + "s");
                Common.tell(getPlayer(), "&7Reason: &c&o" + reason);
                Common.tell(getPlayer(), "&7");
            }
            Common.tell(getPlayer(), "&8&8&l&m------------&8&l&m------------&8&l&m------------");
        }
        else {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bImpoper syntax: &f/mutehistory [Player] <Entries>");
            return;
        }
    }
}
