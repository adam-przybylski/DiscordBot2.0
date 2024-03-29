package com.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class LeaveCommand implements Command {

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.getChannel().sendMessage("You need to be in the voice channel for this command to work").queue();
            return;
        }

        if (event.getGuild().getAudioManager().isConnected()) {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.closeAudioConnection();
        }
        event.getHook().sendMessage("Left").queue();
    }

    @Override
    public String getDescription() {
        return "Leave the voice channel";
    }

    @Override
    public String getName() {
        return "leave";
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
