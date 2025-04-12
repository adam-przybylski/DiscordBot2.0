package com.bot.commands.music;

import com.bot.commands.Command;
import com.bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleQueueCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        List<AudioTrack> queue = new ArrayList<>(PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.stream().toList());
        Collections.shuffle(queue);
        PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
        PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.addAll(queue);
        event.getHook().sendMessage("Shuffled the queue").queue();
    }

    @Override
    public String getDescription() {
        return "Shuffle queue order";
    }

    @Override
    public String getName() {
        return "shuffle";
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
