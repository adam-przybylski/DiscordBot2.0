package com.bot.commands;

import com.bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class ShowQueueCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        Object[] queue = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.toArray();
        StringBuilder sb = new StringBuilder();
        sb.append("Queue:\n");
        for (Object object : queue) {
            AudioTrack track = (AudioTrack) object;
            sb.append(track.getInfo().title).append("\n");
        }
        event.getHook().sendMessage(sb.toString()).queue();
    }

    @Override
    public String getDescription() {
        return "Show songs in queue";
    }

    @Override
    public String getName() {
        return "queue";
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
