package com.veryrarejosh.veryrarebot;

import com.veryrarejosh.veryrarebot.commands.ContextMenuCommands;
import com.veryrarejosh.veryrarebot.commands.GeneralCommands;
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

    private final ShardManager shardManager;
    private final Dotenv config;

    /**
     * Builds bot shards and registers commands and modules
     *
     * @throws LoginException occurs if bot token is invalid.
     */
    public veryrarebot() throws LoginException {
        config = Dotenv.configure().ignoreIfMissing().load();

        String token;
        if (config == null) {
            token = System.getenv().get("TOKEN");
        } else {
            token = config.get("TOKEN");
        }

        // Setup shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("/help"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        shardManager = builder.build();

        //Register listeners
        shardManager.addEventListener(
                new ContextMenuCommands(),
                new GeneralCommands());
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