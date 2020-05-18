package org.edgegamers.picklez.Commands.NoteCommands;

import org.bukkit.Bukkit;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class GetNotesCommand extends SimpleCommand {

    //HOW A WARN IS STORED
    //[Warner]@[Date]@[Reason]


    public GetNotesCommand() {
        super("notes");
        setPermission("cpas.getnotes");
        setDescription("Used to fetch cross-server notes of a player");
        setUsage("/notes [Username] [Amount]");
    }

    @Override
    public void onCommand() {
        if(args.length == 0) {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou need to specify a player to get notes of!");
        }
        else if(args.length == 1) {

            if(Bukkit.getPlayer(args[0]) == null) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat player could not be found!");
                return;
            }

            //No amount specified
            List<String> notes = CpasPlayerCache.getCache(Bukkit.getPlayer(args[0]).getUniqueId()).getNotes();

            if(notes == null || notes.size() == 0) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat player does not have any notes!");
                return;
            }

            Common.tell(getPlayer(), "&8&l&m---------------[&r &c&lPlayer &7Notes &8&l&m]---------------");
            Common.tell(getPlayer(), "&7");
            for(int count = 0; (count < notes.size()) && (count <= 5); count++) {
                String[] divided = notes.get(count).split("@");
                String warner = divided[0];
                String date = divided[1];
                String reason = divided[2];

                Common.tell(getPlayer(), "&8(&c" + count + "&8) " + date + " &7-  &c" + warner + "&7 noted &c" + getPlayer().getName() + " &7for: &7&o" + reason);
            }
            Common.tell(getPlayer(), "");
            Common.tell(getPlayer(), "&8&l&m---------------------------------------------");
        }
        else if(args.length == 2) {

            if(Bukkit.getPlayer(args[0]) == null) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat player could not be found!");
                return;
            }


            int limit = Integer.parseInt(args[1]) - 1;
            //No amount specified
            List<String> notes = CpasPlayerCache.getCache(Bukkit.getPlayer(args[0]).getUniqueId()).getNotes();

            if(notes == null || notes.size() == 0) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat player does not have any notes!");
                return;
            }

            Common.tell(getPlayer(), "&8&l&m---------------[&r &c&lPlayer &7Notes &8&l&m]---------------");
            Common.tell(getPlayer(), "&7");
            for(int count = 0; (count < notes.size()) && (count <= limit); count++) {
                String[] divided = notes.get(count).split("@");
                String warner = divided[0];
                String date = divided[1];
                String reason = divided[2];

                Common.tell(getPlayer(), "&8(&c" + count + "&8) " + date + " &7-  &c" + warner + "&7 noted &c" + getPlayer().getName() + " &7for: &7&o" + reason);
            }
            Common.tell(getPlayer(), "");
            Common.tell(getPlayer(), "&8&l&m---------------------------------------------");
        }
        else {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bUsage: /notes [Player] <Amount>");
        }
    }
}
