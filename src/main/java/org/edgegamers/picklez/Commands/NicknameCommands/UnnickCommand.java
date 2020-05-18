package org.edgegamers.picklez.Commands.NicknameCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

public class UnnickCommand extends SimpleCommand {

    public UnnickCommand() {
        super("unnick");
        setUsage("/unnick [player]");
        setPermission("cpas.unnick");
        setAutoHandleHelp(true);
    }

    @Override
    public void onCommand() {
        if(args.length > 0) {
            //Runs if unnicking another player
            if(getPlayer().hasPermission("cpas.unnick.others")) {
                //Runs if they have nessesary permissions
                String playerName = args[0];
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(player.getName().equalsIgnoreCase(playerName) || player.getDisplayName().equalsIgnoreCase(playerName)) {
                        player.setDisplayName(player.getName());
                        CpasPlayerCache data = CpasPlayerCache.getCache(player.getUniqueId());
                        data.setNickName(player.getName());
                        Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bPlayer's nickname has been reset!");
                    }
                }
            }
            else {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou don't have the ability to unnick another player!");
            }
        }
        else {
            //Runs if unnicking yourself
            CpasPlayerCache data = CpasPlayerCache.getCache(getPlayer().getUniqueId());
            data.setNickName(getPlayer().getName());

            getPlayer().setDisplayName(getPlayer().getName());

            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou have reset your nickname!");
        }
    }
}
