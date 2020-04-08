package org.edgegamers.picklez.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Storage.PlayerData;
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
                        PlayerData data = new PlayerData(player.getUniqueId().toString());
                        data.setNickname(player.getName());
                        data.setSearch(player.getName() + " | " + player.getName());
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
            PlayerData data = new PlayerData(getPlayer().getUniqueId().toString());
            data.setNickname(getPlayer().getName());
            data.setSearch(getPlayer().getName() + " | " + getPlayer().getName());

            getPlayer().setDisplayName(getPlayer().getName());

            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou have reset your nickname!");
        }
    }
}
