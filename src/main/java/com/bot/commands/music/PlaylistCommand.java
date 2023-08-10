package com.bot.commands.music;

import com.bot.commands.Command;
import com.bot.lavaplayer.PlayerManager;
import com.bot.utils.Utils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class PlaylistCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        Utils.joinVoiceChannel(event);
        String link = event.getOption("url").getAsString();
        if (!Utils.isUrl(link)) {
            event.getHook().sendMessage("Invalid playlist link").queue();
        }
        PlayerManager.getInstance().playPlaylist(event, link);
        event.getHook().sendMessage("Adding to queue:").queue();
    }

    @Override
    public String getDescription() {
        return "Play a youtube playlist";
    }

    @Override
    public String getName() {
        return "playlist";
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(getOptions());
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "url", "Link to the playlist", true)
        );
    }
}
