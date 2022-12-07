package com.veryrarejosh.veryrarebot.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class for general commands.
 *
 * @author veryrarejosh
 * @since 11/17/22
 */
public class GeneralCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "welcome" -> {
                String userTag = event.getUser().getAsTag();
                event.reply("Welcome to the server, **" + userTag + "**!").queue();
            }
            case "roles" -> {
                event.deferReply().queue();
                StringBuilder response = new StringBuilder();
                for (Role role : Objects.requireNonNull(event.getGuild()).getRoles()) {
                    response.append(role.getAsMention()).append("\n");
                }
                event.getHook().sendMessage(response.toString()).queue();
            }
            case "say" -> {
                OptionMapping messageOption = event.getOption("message");
                assert messageOption != null;
                String message = messageOption.getAsString();
                event.getChannel().sendMessage(message).queue();
                event.reply("Your message was sent").setEphemeral(true).queue();
            }
            case "roll" -> {
                OptionMapping integerOption = event.getOption("max");
                int min = 1;
                assert integerOption != null;
                int max = integerOption.getAsInt();
                int range = (max - min) + 1;
                event.reply(Integer.toString((int) (Math.random() * range) + min)).queue();
            }
            case "help" -> event.reply("List of Commands\nhttps://github.com/jxshie/veryrarebot\n").queue();
            case "pfp" -> {
                OptionMapping userOption = event.getOption("user");
                if (userOption == null) {
                    String userAvatar = Objects.requireNonNull(event.getMember()).getUser().getAvatarUrl();
                    assert userAvatar != null;
                    event.reply(userAvatar).complete();
                } else {
                    String mentionAvatar = userOption.getAsUser().getAvatarUrl();
                    assert mentionAvatar != null;
                    event.reply(mentionAvatar).complete();
                }
            }
            case "ping" -> event.reply("Pong!").queue();
            case "howdy" -> event.reply("Howdy!").queue();
        }
    }


    /*
     * Registers commands on bot startup
     *
     * @author veryrarejosh
     * @since 11/17/22
     */
    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by lovely, veryrarebot."));
        commandData.add(Commands.slash("roles", "Display all available roles on the server."));
        commandData.add(Commands.slash("help", "Sends a link to link of all the bot commands"));
        commandData.add(Commands.slash("ping", "Pong!"));
        commandData.add(Commands.slash("howdy", "Howdy!"));

        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say.", true);
        OptionData option2 = new OptionData(OptionType.INTEGER, "max", "The maximum number of your roll.", true);
        OptionData option3 = new OptionData(OptionType.USER, "user", "The user whose avatar you want.", false);
        commandData.add(Commands.slash("say", "Make the bot say a message.").addOptions(option1));
        commandData.add(Commands.slash("roll", "Get the bot to roll a dice for you").addOptions(option2));
        commandData.add(Commands.slash("pfp", "Grab your own profile picture, or another persons.").addOptions(option3));

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
