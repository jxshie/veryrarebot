package com.veryrarejosh.veryrarebot.commands;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ContextMenuCommands extends ListenerAdapter {
    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        if (event.getName().equals("Grab user avatar")) {
            event.reply(event.getTarget().getEffectiveAvatarUrl()).queue();
        }
    }
}
