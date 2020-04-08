package org.edgegamers.picklez.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

public class RealnameCommand extends SimpleCommand {

    public RealnameCommand() {
        super("realname");
        setPermission("cpas.realname");
        setUsage("/realname [playerName]");
    }

    @Override
    public void onCommand() {
        if(args.length != 0) {
            String pName = args[0];

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.getDisplayName().equalsIgnoreCase(pName)) {
                    Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &f" + args[0] +"'s &b real name is: &f&o" + player.getName());
                }
            }

        }
        else {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bPlease specify a player's nickname");
            return;
        }
    }
}
