package com.veryrarejosh.veryrarebot.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EventListener extends ListenerAdapter {


    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();

        assert user != null;
        String message = user.getAsTag() + " reacted to a message with " + emoji + " in " + channelMention + ".";
        Objects.requireNonNull(event.getGuild().getTextChannelById("1042993739863429160")).sendMessage(message).queue();
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        String command = message[0];
        if (command.equalsIgnoreCase("!clear")) {
            boolean isAdmin = false;
            for (Role role : Objects.requireNonNull(event.getMember()).getRoles()) {
                if (role.getName().equals("Josh")) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) {
                event.getChannel().sendMessage("You can't run this command because you're not an admin!").queue();
            } else {
                if (message.length == 1) {
                    event.getChannel().getHistory().retrievePast(11)
                            .queue(m -> { // success callback
                                event.getChannel().purgeMessages(m);
                            });
                } else if (message.length == 2) {
                    event.getChannel().getHistory().retrievePast(Integer.parseInt(message[1] + 1))
                            .queue(m -> { // success callback
                                event.getChannel().purgeMessages(m);
                            });
                }
            }
        }

    }
}

