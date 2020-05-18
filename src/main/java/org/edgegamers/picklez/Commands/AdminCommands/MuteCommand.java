package org.edgegamers.picklez.Commands.AdminCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MuteCommand extends SimpleCommand {

    public MuteCommand() {
        super("mute");
        setPermission("cpas.mute");
        setUsage("/mute [Player] [Time] [Reason]");
    }

    @Override
    public void onCommand() {

        if(!(args.length < 3)) {
            //args[0] == player
            //args[1] == time
            //args[2:] == reason
            CpasPlayerCache storage = CpasPlayerCache.getCache(Bukkit.getOfflinePlayer(args[0]).getUniqueId());
            List<String> mutes = storage.getMutes();

            int referenceNumber = mutes.size() + 1;

            LocalDateTime dateTimeOne = LocalDateTime.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
            String date = dateTimeOne.format(dateFormat);


            int duration = getDuration(args[1]);
            String muter = getPlayer().getName();
            boolean isActive = true;
            String reason = "";

            for(int i = 2; i < args.length; i++) {
                reason += args[i] + " ";
            }

            String format = "" + referenceNumber + "@" + date + "@" + duration + "@" + muter + "@" + isActive + "@" + reason;

            mutes.add(format);

            storage.setMutes(mutes);

            if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                Player player = Bukkit.getPlayer(args[0]);
                Common.tell(player, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou have been muted by: &f" + getPlayer().getName() + "&b for reason: &f&o" + reason + "&r&b. &bDuration: &f" + args[1]);
            }

            Common.tell(getPlayer(),"&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou have muted: &f" + args[0] + "&b for reason: &f&o" + reason + "&r&b. &bDuration: &f" + args[1]);
        }
        else {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &8\u00BB &7Improper syntax! Usage: &f/mute [Player] [Time] [Reason]");
        }

    }

    private int getDuration(String durationString) {
        int duration = 0;
        String preDuration = durationString; //1h, 1d, 1y
        if(preDuration.contains("y")) {
            preDuration = preDuration.replaceAll("y", "");
            duration = Integer.parseInt(preDuration);
            duration *= 525600;
        }
        else if(preDuration.contains("M")) {
            preDuration = preDuration.replaceAll("M", "");
            duration = Integer.parseInt(preDuration);
            duration *= 43800;
        }
        else if(preDuration.contains("d")) {
            preDuration = preDuration.replaceAll("d", "");
            duration = Integer.parseInt(preDuration);
            duration *= 1440;
        }
        else if(preDuration.contains("h")) {
            preDuration = preDuration.replaceAll("h", "");
            duration = Integer.parseInt(preDuration);
            duration *= 60;
        }
        else if(preDuration.contains("m")){
            preDuration = preDuration.replaceAll("m", "");
            duration = Integer.parseInt(preDuration);
        }
        else {
            duration = Integer.parseInt(preDuration);
        }

        duration = duration *60;

        return duration;
    }
}
