package com.bot.commands;

import com.bot.commands.music.*;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    List<Command> commands = List.of(
            new SayCommand(),
            new PlayCommand(),
            new SkipCommand(),
            new SkipAllCommand(),
            new JoinCommand(),
            new LeaveCommand(),
            new ShowQueueCommand(),
            new NowPlayingCommand(),
            new PauseCommand(),
            new ResumeCommand(),
            new PlaylistCommand(),
            new LoopCommand(),
            new ReplayCommand(),
            new LocalTrackCommand(),
            new YtSearchCommand(),
            new ScSearchCommand(),
            new LoadConfigCommand(),
            new DeleteFromQueueCommand(),
            new ShuffleQueueCommand()
    );

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (Command command : commands) {
            if (event.getName().equals(command.getName())) {
                command.handle(event);
            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        registerCommands(event);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        registerCommands(event);
    }

    private void registerCommands(GenericGuildEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        for (Command command : commands) {
            commandData.add(command.getCommandData());
        }
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
