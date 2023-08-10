package com.bot.commands.music;

import com.bot.commands.Command;
import com.bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class ReplayCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        String result = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.repeatTrack();
        if (result == null) {
            event.getHook().sendMessage("No song to replay").queue();
        } else {
            event.getHook().sendMessage("Replaying" + result).queue();
        }
    }

    @Override
    public String getDescription() {
        return "Replay the last song";
    }

    @Override
    public String getName() {
        return "replay";
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
