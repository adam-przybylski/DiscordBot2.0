package com.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class SayCommand implements Command {

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (event.getName().equals("say")) {
            OptionMapping option = event.getOption("message");
            if (option == null) {
                return;
            }
            String message = option.getAsString();
            event.reply(message).queue();
        }
    }

    @Override
    public String getDescription() {
        return "Make the bot say a message";
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(getOptions());
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "message", "The message that you want the bot to say", true)
        );
    }
}
