package com.bot.commands;

import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("test")) {
            event.deferReply().queue();
            event.getHook().sendMessage("You just used test command").queue();
            //event.reply("You just used test command").queue();
        } else if (event.getName().equals("say")) {
            OptionMapping option = event.getOption("message");
            String message = option.getAsString();
            event.reply(message).queue();
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
        commandData.add(Commands.slash("test", "Test command"));

        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message that you want the bot to say", true);
        commandData.add(Commands.slash("say", "Make the bot say a message")
                .addOptions(option1));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
