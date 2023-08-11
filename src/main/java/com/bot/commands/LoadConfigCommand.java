package com.bot.commands;

import com.bot.Config;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class LoadConfigCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Config.getInstance().loadConfig();
        event.reply("Config reloaded").queue();
    }

    @Override
    public String getDescription() {
        return "Load bot configuration";
    }

    @Override
    public String getName() {
        return "load_config";
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(getOptions());
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }
}
