package com.bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.collections4.map.HashedMap;

import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        musicManagers = new HashedMap<>();

        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                String message = "Adding to queue **"
                        .concat(audioTrack.getInfo().title)
                        .concat("** by **")
                        .concat(audioTrack.getInfo().author)
                        .concat("** **(")
                        .concat(String.valueOf(audioTrack.getInfo().length / 1000 / 60))
                        .concat(":")
                        .concat(String.valueOf(audioTrack.getInfo().length / 1000 % 60))
                        .concat(")**");
                textChannel.sendMessage(message).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {
                    musicManager.scheduler.queue(tracks.get(0));
                    String message = "Adding to queue **"
                            .concat(tracks.get(0).getInfo().title)
                            .concat("** by **")
                            .concat(tracks.get(0).getInfo().author)
                            .concat("** **(")
                            .concat(String.valueOf(tracks.get(0).getInfo().length / 1000 / 60))
                            .concat(":")
                            .concat(String.valueOf(tracks.get(0).getInfo().length / 1000 % 60))
                            .concat(")**");
                    textChannel.sendMessage(message).queue();
                }
            }

            @Override
            public void noMatches() {
                textChannel.sendMessage("No matches found").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                textChannel.sendMessage("Failed to play a track").queue();
                e.printStackTrace();
            }
        });
    }

    public void skipTrack(Guild guild) {
        final GuildMusicManager musicManager = this.getMusicManager(guild);
        musicManager.scheduler.nextTrack();
    }

    public void skipAllTracks(Guild guild) {
        final GuildMusicManager musicManager = this.getMusicManager(guild);
        musicManager.scheduler.queue.clear();
        musicManager.scheduler.nextTrack();
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
