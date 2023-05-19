package net.mauki.maukiseasonpl.discord.commands.handler;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.discord.commands.utils.LinkCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlashCommandHandler extends ListenerAdapter {

    public static List<SlashCommand> slashCommandList = new ArrayList<>();
    public static List<SubCommand> subCommandList = new ArrayList<>();
    public static List<SlashCommand> slashCommandOfSubCommandList = new ArrayList<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        AtomicBoolean performed = new AtomicBoolean(false);
        for (SlashCommand slashCommand : slashCommandList) if(slashCommand.commandName().equals(event.getName())) {
            try {
                Thread t = new Thread(() -> {
                    try {
                        if(performed.get()) return;
                        slashCommand.handle(event);
                        Boot.getLOGGER().info("Executed \""+event.getName()+"\" (ID: "+event.getId()+" | Slash)");
                        performed.set(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                t.setName("SlashCommandHandler");
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(performed.get()) return;
        for(SlashCommand slashCommand : slashCommandOfSubCommandList) if(slashCommand.commandName().equals(event.getName()+" "+event.getSubcommandName())) {
            try {
                Thread t = new Thread(() -> {
                    try {
                        if(performed.get()) return;
                        slashCommand.handle(event);
                        Boot.getLOGGER().info("Executed \""+event.getName()+"\" (ID: "+event.getId()+" | Sub)");
                        performed.set(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                t.setName("SlashCommandHandler-SubCommand");
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads all commands
     */
    public static void loadCommands() {
        slashCommandList.add(new LinkCommand());

        CommandListUpdateAction commandListUpdateAction = Boot.getDISCORD_CLIENT().getJDA().updateCommands();
        for(SlashCommand slashCommand : slashCommandList){
            commandListUpdateAction.addCommands(slashCommand.commandData()).queue();
            //Boot.getLogger().info("Updated Slash-Command "+slashCommand.commandData().getName());
        }
        for(SubCommand subCommand : subCommandList){
            commandListUpdateAction.addCommands(subCommand.manageData()).queue();
            //Boot.getLogger().info("Updated Sub-Command "+slashCommand.manageData().getName());
        }
        commandListUpdateAction.queue();
        Boot.getLOGGER().info("Slash Commands updated!");

        for(SubCommand sub : subCommandList) slashCommandOfSubCommandList.addAll(sub.getSlashCommands());

    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        for(Guild g : Boot.getDISCORD_CLIENT().getJDA().getGuilds()) {
            CommandListUpdateAction commandListUpdateAction = g.updateCommands();
            for (SlashCommand slashCommand : slashCommandList) commandListUpdateAction.addCommands();
            for (SubCommand subCommand : subCommandList) commandListUpdateAction.addCommands();

            commandListUpdateAction.queue();
        }
        Boot.getLOGGER().info("Guild-slashcommands removed from every guild!");
    }
}