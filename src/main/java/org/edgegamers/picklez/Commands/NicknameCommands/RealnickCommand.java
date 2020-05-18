package org.edgegamers.picklez.Commands.NicknameCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

public class RealnickCommand extends SimpleCommand {

    public RealnickCommand() {
        super("realnick");
        setPermission("cpas.realnick");
        setDescription("Get a nickname from a username");
        setUsage("/realnick [Player]");
    }

    @Override
    public void onCommand() {
        if(args.length != 0) {
            //Player name given
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.getName().equalsIgnoreCase(args[0]) && player.isOnline()) {
                    CpasPlayerCache tempData = CpasPlayerCache.getCache(player.getUniqueId());
                    Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &f" + player.getName() + "'s &bnickname is: &f" + tempData.getNickName());
                    return;
                }
            }
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat player could not be found!");
        }
        else {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bPlease specify a player's name");
        }
    }
}
