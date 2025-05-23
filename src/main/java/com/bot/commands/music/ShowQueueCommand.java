package com.bot.commands.music;

import com.bot.commands.Command;
import com.bot.lavaplayer.PlayerManager;
import com.bot.utils.Utils;
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
        int i = 1;
        for (Object object : queue) {
            AudioTrack track = (AudioTrack) object;
            sb.append("**").append(i++).append(".** ");
            sb.append(Utils.formatTrackInfo(track));
            sb.append("\n");
            if(i > 20) break;
        }
        sb.append("and ").append(queue.length - 20).append(" more");
        event.getChannel().asTextChannel().sendMessage(sb.toString()).queue();
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
