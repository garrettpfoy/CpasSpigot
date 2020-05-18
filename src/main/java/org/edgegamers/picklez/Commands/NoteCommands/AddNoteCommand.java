package org.edgegamers.picklez.Commands.NoteCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class AddNoteCommand extends SimpleCommand {

    //HOW A WARN IS STORED
    //[Warner]@[Date]@[Reason]

    public AddNoteCommand() {
        super("note");
        setPermission("cpas.addnote");
        setUsage("/note [Username] [Note]");
        setDescription("Used to give a note to a player cross-server");
    }

    @Override
    public void onCommand() {

        if(args.length < 2) {
            //Nope!
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bUsage: /note [Username] [Note]");
            return;
        }
        else {
            //Yes!
            if(Bukkit.getPlayer(args[0]) == null) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bSorry, but I couldn't find that player!");
                return;
            }
            else {
                Player warned = Bukkit.getPlayer(args[0]);
                Player warner = getPlayer();

                List<String> notes = CpasPlayerCache.getCache(warned.getUniqueId()).getNotes();

                String reason = "";

                for(int i = 1; i < args.length; i++) {
                    reason += args[i];
                }

                String format = warner.getName() + "@" + java.time.LocalDate.now() + "@" + reason;

                notes.add(format);

                CpasPlayerCache.getCache(warned.getUniqueId()).setNotes(notes);

                Common.tell(warner, "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bYou have added a note to that player!");

            }
        }

    }
}
