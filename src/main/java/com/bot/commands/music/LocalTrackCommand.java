package com.bot.commands.music;

import com.bot.Config;
import com.bot.commands.Command;
import com.bot.lavaplayer.PlayerManager;
import com.bot.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.List;

public class LocalTrackCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        List<String> sounds = (List<String>) Config.getInstance().getConfig().get("sounds");
        if (event.getOption("nr") == null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Local tracks", null);
            eb.setDescription("List of local tracks");
            eb.setColor(new Color(255, 0, 54));
            for (String sound : sounds) {
                eb.addField("/track " + sounds.indexOf(sound), sounds.indexOf(sound) + ". " + sound.replace("_", " "), true);
            }
            eb.setAuthor(Utils.selfUser.getName(), null, Utils.selfUser.getAvatarUrl());
            eb.setFooter("Requested by " + event.getUser().getName(), event.getUser().getAvatarUrl());
            event.getHook().sendMessageEmbeds(eb.build()).queue();
            return;
        }
        Utils.joinVoiceChannel(event);
        int number = event.getOption("nr").getAsInt();
        PlayerManager.getInstance().playLocalTrack(event, "tracks/" + sounds.get(number) + ".mp3");
        event.getHook().sendMessage(sounds.get(number).replace("_", " ")).queue();
    }

    @Override
    public String getDescription() {
        return "Play a local track";
    }

    @Override
    public String getName() {
        return "track";
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(getOptions());
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "nr", "Number of the track", false)
        );
    }
}
