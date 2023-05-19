package net.mauki.maukiseasonpl.discord.commands.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;


/**
 * Interface for SlashCommands
 */
public interface SlashCommand {

    /**
     * Get the command name
     * @return returns the name of the command
     */
    String commandName();

    /**
     * Handle the command with all its information
     * @param event
     *        The event object of the event to get all information for the command
     *
     * @throws Exception will be thrown when there occurred some errors
     */
    void handle(SlashCommandInteractionEvent event) throws Exception;

    /**
     * Get the command data of the slash command
     * @return returns the data of the slash command
     */
    CommandData commandData();

}
