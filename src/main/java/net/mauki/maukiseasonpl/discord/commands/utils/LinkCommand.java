package net.mauki.maukiseasonpl.discord.commands.utils;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.core.LiteSQL;
import net.mauki.maukiseasonpl.discord.commands.handler.SlashCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.UUID;

/**
 * Discord command for linking your accounts
 */
public class LinkCommand implements SlashCommand {

    /**
     * The name of the command
     * @return The name of the command
     */
    @Override
    public String commandName() {
        return "link";
    }

    /**
     * The code which will be executed when command is being called
     * @param event
     *        The event object of the event to get all information for the command
     *
     * @throws Exception Will be thrown when there was an error while executing the command
     */
    @Override
    public void handle(SlashCommandInteractionEvent event) throws Exception {
        String code = Objects.requireNonNull(event.getOption("code")).getAsString();
        ResultSet rs = LiteSQL.onQuery("SELECT * FROM connections WHERE code = '" + code + "'");

        while(rs.next()) {
            if(rs.getString("code").equalsIgnoreCase(code)) {
                Player player = Bukkit.getPlayer(UUID.fromString(rs.getString("uuid")));
                LiteSQL.onUpdate("UPDATE connections SET discord_id = '" + Objects.requireNonNull(event.getMember()).getId() + "' WHERE code = '" + code + "'");
                event.reply(":white_check_mark: | **" + (player == null ? "_Name konnte nicht geladen werden_" : player.getName()) + "** wurde erfolgreich als dein Minecraft-Account hinterlegt.").setEphemeral(true).queue();
                return;
            }
            event.reply(":x: | Invalider Code!").setEphemeral(true).queue();
        }
    }

    /**
     * The command data of the slashcommand
     * @return The command data of the slashcommand
     */
    @Override
    public CommandData commandData() {
        return Commands.slash("link", "Verbinde deinen Minecraft Account mit deinem Discord Account.")
                .addOption(OptionType.STRING, "code", "Der Code, welcher dir in Minecraft zugeschickt wurde.", true, false);
    }
}
