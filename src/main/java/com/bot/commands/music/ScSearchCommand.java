package com.bot.commands.music;

import com.bot.commands.Command;
import com.bot.lavaplayer.PlayerManager;
import com.bot.lavaplayer.TrackScheduler;
import com.bot.utils.Utils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class ScSearchCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        if(event.getOption("title") == null && event.getOption("number") == null){
            event.getHook().sendMessage("Please enter a song name").queue();
            return;
        }
        if(event.getOption("number") != null){
            Utils.joinVoiceChannel(event);
            int number = Integer.parseInt(event.getOption("number").getAsString());
            number--;
            if (number > 4) number = 4;
            if (number < 0) number = 0;
            TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler;
            scheduler.playFromSearchList(number);
            event.getHook().sendMessage("Adding to queue:" + Utils.formatTrackInfo(scheduler.tracksFromSearch.get(number))).queue();
            return;
        }
        String link = "scsearch: " + event.getOption("title").getAsString();
        PlayerManager.getInstance().search(event, link);
        event.getHook().sendMessage("Search results: (type /scsearch <number> to play)\n").queue();
    }

    @Override
    public String getDescription() {
        return "Search for a song on SoundCloud";
    }

    @Override
    public String getName() {
        return "scsearch";
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(getOptions());
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "title", "Name of the song", false),
                new OptionData(OptionType.STRING, "number", "Number of the song from list", false)
        );
    }
}
