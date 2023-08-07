package com.bot;

import com.bot.commands.CommandManager;
import com.bot.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;

public class Bot {

    private final ShardManager shardManager;

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Bot() throws LoginException {
        Dotenv config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing((String) Config.getInstance().getConfig().get("activity_status")));

        List<GatewayIntent> intents = Arrays.asList(
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.MESSAGE_CONTENT
        );
        builder.enableIntents(intents);
//        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
//        builder.setChunkingFilter(ChunkingFilter.ALL);
//        builder.enableCache(CacheFlag.ONLINE_STATUS);

        shardManager = builder.build();

        // Register listener
        shardManager.addEventListener(new EventListener(), new CommandManager());
    }

    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
        } catch (LoginException e) {
            System.err.println("ERROR: Provided bot token is invalid");
        }
    }
}
