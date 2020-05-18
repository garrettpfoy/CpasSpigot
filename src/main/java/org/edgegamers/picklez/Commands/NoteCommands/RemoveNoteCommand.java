package org.edgegamers.picklez.Commands.NoteCommands;

import org.bukkit.Bukkit;
import org.edgegamers.picklez.Storage.CpasPlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class RemoveNoteCommand extends SimpleCommand {

    public RemoveNoteCommand() {
        super("unnote");
        setPermission("cpas.removenote");
        setUsage("/unnote [Player] [Index]");
        setDescription("Used to remove a note from a player");
    }

    @Override
    public void onCommand() {
        if(args.length != 2) {
            Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bUsage: /unnote [Player] [Index]");
            return;
        }
        else {
            if(Bukkit.getPlayer(args[0]) == null) {
                Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bSorry but that player could not be found!");
            }
            else {
                List<String> notes = CpasPlayerCache.getCache(Bukkit.getPlayer(args[0]).getUniqueId()).getNotes();

                if(Integer.parseInt(args[1]) >= notes.size()) {
                    Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bThat index was out of range!");
                    return;
                }
                else {
                    notes.remove(Integer.parseInt(args[1]));

                    CpasPlayerCache.getCache(Bukkit.getPlayer(args[0]).getUniqueId()).setNotes(notes);

                    Common.tell(getPlayer(), "&1&lE&9&lG&f&lO &c&lMAUL &f&l\u00BB &bI have removed the note at that index!");
                }

            }
        }
    }
}
