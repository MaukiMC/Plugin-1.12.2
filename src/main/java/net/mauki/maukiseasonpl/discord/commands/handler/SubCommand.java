package net.mauki.maukiseasonpl.discord.commands.handler;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;

/**
 * Interface for SubCommands
 */
public interface SubCommand {

    /**
     * The list of the slashcommand
     */
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

    /**
     * Get the {@link ArrayList} with the slashcommands
     * @return The {@link ArrayList} of the slashcommands
     */
    default ArrayList<SlashCommand> getSlashCommands() {
        return slashCommands;
    }

}
