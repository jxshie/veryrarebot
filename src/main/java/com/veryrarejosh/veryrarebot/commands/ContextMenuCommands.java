package com.veryrarejosh.veryrarebot.commands;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class ContextMenuCommands extends ListenerAdapter {
    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        if (event.getName().equals("Grab user avatar")) {
            event.reply(event.getTarget().getEffectiveAvatarUrl()).queue();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.context(Command.Type.USER, "Grab user avatar"));

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}

