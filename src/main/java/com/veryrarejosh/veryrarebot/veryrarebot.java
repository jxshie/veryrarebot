package com.veryrarejosh.veryrarebot;

import com.veryrarejosh.veryrarebot.commands.commandManager;
import com.veryrarejosh.veryrarebot.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

/**
 * Main class for veryrarebot Discord Bot.
 * Initializes shard manager, database, and listeners.
 *
 * @author veryrarejosh
 * @since 11/17/22
 */
public class veryrarebot {

    private final Dotenv config;
    private final ShardManager shardManager;

    /**
     * Builds bot shards and registers commands and modules
     *
     * @throws LoginException occurs if bot token is invalid.
     */
    public veryrarebot() throws LoginException {
        String token;
        if (System.getenv("TOKEN") != null) {
            config = null;
            token = System.getenv("TOKEN");
        } else {
            config = Dotenv.configure().load();
            token = config.get("TOKEN");
        }

        // Setup shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("/help"));
        builder.enableIntents(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.MESSAGE_CONTENT);
        shardManager = builder.build();

        //Register listeners
        shardManager.addEventListener(
                new EventListener(),
                new commandManager());
    }

    /**
     * Retrieves the token from .env
     *
     * @return the token from .env for the bot.
     */
    public Dotenv getConfig() {
        return config;
    }

    /**
     * Retrieves the bots manager shard
     *
     * @return the ShardManager instance for the bot.
     */
    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try {
            veryrarebot bot = new veryrarebot();
        } catch (LoginException e) {
            System.out.println("ERROR - Provided bot token is invalid!");
        }

    }
}