package org.edgegamers.picklez.Commands.AdminCommands;

import org.bukkit.Bukkit;
import org.edgegamers.picklez.Storage.PlayerData;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

public class KickCommand extends SimpleCommand {

    public KickCommand() {
        super("kick");
        setPermission("cpas.kick");
        setUsage("/kick [Player] <Reason>");
        setDescription("Used to kick a player from a server");
    }

    @Override
    public void onCommand() {
        if(args.length <= 1) {
            //Display help message
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bUsage: &f/kick [Player] <Reason>");
            return;
        }
        else {
            PlayerData kicker = new PlayerData(getPlayer().getUniqueId().toString());
            PlayerData kicked = new PlayerData(Bukkit.getPlayer(args[0]).getUniqueId().toString());

            if(kicker.getRank() < kicked.getRank()) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou are not able to kick players with a higher rank then you");
                return;
            }
            else {
                String reason = "";
                ///ban [player](0) [time](1) [reason](2+)
                for (int i = 1; i < args.length; i++) {
                    reason += "" + args[i] + " ";
                }

                String kickMessage = Common.colorize("&cYou were kicked from this server!\n\n&cKicked by: &7" + kicker.getForumName() + "\n&cReason: &7&o" + reason + "\n\n&7Report Abuse at: &f&nhttps://edge-gamers.com");

                Bukkit.getPlayer(args[0]).kickPlayer(kickMessage);
                Common.broadcast("&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &f" + kicked.getName() + "&b was kicked by &f" + kicker.getForumName() + "&b for: &f" + reason);
                return;
            }
        }
    }
}
