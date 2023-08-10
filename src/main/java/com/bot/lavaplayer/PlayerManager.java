package com.bot.lavaplayer;

import com.bot.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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

    public void play(SlashCommandInteractionEvent event, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                String message = Utils.formatTrackInfo(audioTrack);
                event.getChannel().asTextChannel().sendMessage(message).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {
                    musicManager.scheduler.queue(tracks.get(0));
                    String message = Utils.formatTrackInfo(tracks.get(0));
                    event.getChannel().asTextChannel().sendMessage(message).queue();
                }
            }

            @Override
            public void noMatches() {
                event.getChannel().asTextChannel().sendMessage("No matches found").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.getChannel().asTextChannel().sendMessage("Failed to play a song").queue();
                e.printStackTrace();
            }
        });
    }

    public void playLocalTrack(SlashCommandInteractionEvent event, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());
        this.audioPlayerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
            }

            @Override
            public void noMatches() {
                if (event != null) {
                    event.getChannel().asTextChannel().sendMessage("No matches found").queue();
                }
            }

            @Override
            public void loadFailed(FriendlyException e) {
                if (event != null) {
                    event.getChannel().asTextChannel().sendMessage("Failed to play a track").queue();
                }
                e.printStackTrace();
            }
        });
    }

    public void playPlaylist(SlashCommandInteractionEvent event, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                String message = Utils.formatTrackInfo(audioTrack);
                event.getChannel().asTextChannel().sendMessage(message).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {
                    StringBuilder message = new StringBuilder();
                    int i = 1;
                    for (AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                        message.append("**").append(i++).append(".** ");
                        message.append(Utils.formatTrackInfo(track));
                        message.append("\n");
                    }
                    event.getChannel().asTextChannel().sendMessage(message.toString()).queue();
                }
            }

            @Override
            public void noMatches() {
                event.getChannel().asTextChannel().sendMessage("No matches found").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.getChannel().asTextChannel().sendMessage("Failed to load a playlist").queue();
                e.printStackTrace();
            }
        });
    }

    public void search(SlashCommandInteractionEvent event, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {
                    StringBuilder message = new StringBuilder();
                    TrackScheduler scheduler = musicManager.scheduler;
                    scheduler.tracksFromSearch.clear();
                    for (int i = 0; i < 5; i++) {
                        message.append("**").append(i+1).append(".** ");
                        message.append(Utils.formatTrackInfo(tracks.get(i)));
                        message.append("\n");
                        scheduler.tracksFromSearch.add(tracks.get(i));
                    }
                    event.getChannel().asTextChannel().sendMessage(message.toString()).queue();
                }
            }

            @Override
            public void noMatches() {
                event.getChannel().asTextChannel().sendMessage("No matches found").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.getChannel().asTextChannel().sendMessage("Failed to search for songs").queue();
                e.printStackTrace();
            }
        });
    }

    public void skipTrack(Guild guild) {
        final GuildMusicManager musicManager = this.getMusicManager(guild);
        musicManager.scheduler.onTrackEnd(musicManager.audioPlayer, musicManager.audioPlayer.getPlayingTrack(), AudioTrackEndReason.FINISHED);
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

    public void playLocalTrack(Guild guild, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(guild);
        this.audioPlayerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException e) {
                e.printStackTrace();
            }
        });
    }
}
