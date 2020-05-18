package org.edgegamers.picklez.Commands.AdminCommands;

import org.bukkit.Bukkit;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class UnMuteCommand extends SimpleCommand {

    public UnMuteCommand() {
        super("unmute");
        setPermission("cpas.unmute");
        setUsage("/unmute [Player]");
    }

    @Override
    public void onCommand() {
        if(args.length != 1) {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bImproper syntax: &f/unmute [Player]");
        }
        else {
            CpasPlayerCache storage = CpasPlayerCache.getCache(Bukkit.getOfflinePlayer(args[0]).getUniqueId());

            List<String> mutes = storage.getMutes();

            if(mutes.size() == 0) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat player doesn't have any mutes on record!");
            }

            for(String muteFormatted : mutes) {
                //Store mute as string
                //[REFERENCE#]@[DATE]@[DURATION]@[MUTER]@[isACTIVE]@[REASON]
                String[] formatted = muteFormatted.split("@");

                if(Boolean.parseBoolean(formatted[4])) {
                    //Active mute, lets remove it
                    String newFormat = formatted[0] + "@" + formatted[1] + "@"  + formatted[2] + "@"  + formatted[3] + "@"  + false + "@"  + formatted[5];
                    mutes.set(Integer.parseInt(formatted[0]) - 1, newFormat);
                }
            }
            storage.setMutes(mutes);

            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bPlayer has been unmuted!");

            if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                Common.tell(Bukkit.getPlayer(args[0]), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou have been unmuted!");
            }

        }
    }
}
