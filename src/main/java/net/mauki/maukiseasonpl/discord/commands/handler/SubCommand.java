package net.mauki.maukiseasonpl.discord.commands.handler;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;

public interface SubCommand {

    ArrayList<SlashCommand> slashCommands = new ArrayList<>();

    /**
     * Get the command name
     * @return returns the name of the command
     */
    String commandName();

    /**
     * Get the command data of the slash command
     * @return returns the data of the slash command
     */
    CommandData manageData();

    default ArrayList<SlashCommand> getSlashCommands() {
        return slashCommands;
    }

}
