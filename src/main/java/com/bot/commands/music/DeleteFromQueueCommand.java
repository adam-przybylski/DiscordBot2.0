package com.bot.commands.music;

import com.bot.commands.Command;
import com.bot.lavaplayer.PlayerManager;
import com.bot.lavaplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DeleteFromQueueCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler;
        Object[] queue = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.toArray();
        int position = event.getOption("number").getAsInt();
        if (position < 1) position = 1;
        if (position > queue.length) position = queue.length;
        BlockingQueue<AudioTrack> newQueue = new LinkedBlockingQueue<>();
        for (int i = 0; i < queue.length; i++) {
            if (i != position - 1) {
                newQueue.add((AudioTrack) queue[i]);
            }
        }
        scheduler.queue = newQueue;
        event.getHook().sendMessage("Deleted song from queue at position " + position).queue();
    }

    @Override
    public String getDescription() {
        return "Delete a song from the queue";
    }

    @Override
    public String getName() {
        return "qdel";
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(getOptions());
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "number", "Position of song to delete", true)
        );
    }
}
