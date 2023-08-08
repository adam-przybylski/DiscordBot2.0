package com.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface Command {

    void handle(SlashCommandInteractionEvent event);

    String getDescription();

    String getName();

    CommandData getCommandData();

    List<OptionData> getOptions();
}
