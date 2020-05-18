package org.edgegamers.picklez.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class onPlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws ParseException {

        CpasPlayerCache storage = CpasPlayerCache.getCache(event.getPlayer().getUniqueId());

        List<String> mutes = storage.getMutes();

        if(mutes.size() > 0) {
            //Has at least one mute on record
            for(String muteFormatted : mutes) {
                String[] args = muteFormatted.split("@");
                if(Boolean.parseBoolean(args[4])) {
                    //Mute is active, lets check if they should still be muted
                    if(isStillMuted(muteFormatted)) {
                        //Mute is not over yet, don't modify it at all, however let's cancel the chat event
                        event.setCancelled(true);
                        Common.tell(event.getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou are currently muted! You may not speak!");
                        //Choosing to return because in THEORY the mute that triggers isStillMuted should be relatively new
                        //and since we modify mutes that are passed already, we don't have to worry about the other ones
                        //getting like overlooked in theory
                        return;
                    }
                    else {
                        //Mute is over! Let's modify the mute stored
                        String format = args[0] + "@" + args[1] + "@"  + args[2] + "@"  + args[3] + "@"  + "false" + "@"  + args[5];
                        //Reference number is 1+ index so lets set the format at that index to new format (false isActive)
                        mutes.set(Integer.parseInt(args[0]) - 1, format);
                        //Have to save here in case we return in above logic check
                        storage.setMutes(mutes);
                    }
                }
            }

        }
    }


    private boolean isStillMuted(String muteFormatted) throws ParseException {
        String[] args = muteFormatted.split("@");

        Date startDate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(args[1]);

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

        String currentDateString = formatter.format(new Date());

        Date currentDate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(currentDateString);

        long period = TimeUnit.SECONDS.toSeconds(currentDate.getTime() - startDate.getTime());

        period = period / 1000;

        int duration = Integer.parseInt(args[2]);

        if(period >= duration) {
            //Time since mute is greater then duration, no longer should be muted
            return false;
        }
        else {
            return true;
        }

    }
}
