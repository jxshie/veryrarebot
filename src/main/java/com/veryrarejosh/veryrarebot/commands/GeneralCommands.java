package com.veryrarejosh.veryrarebot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

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
            case "say" -> {
                OptionMapping messageOption = event.getOption("message");
                assert messageOption != null;
                String message = messageOption.getAsString();
                event.getChannel().sendMessage(message).queue();
                event.reply("Your message was sent").setEphemeral(true).queue();
            }
            case "roll" -> {
                OptionMapping integerOption1 = event.getOption("max");
                OptionMapping integerOption2 = event.getOption("dice");

                assert integerOption2 != null;
                int dice = integerOption2.getAsInt();

                StringBuilder list = new StringBuilder();

                for (int counter = 0; counter < dice; counter++) {
                    String[] numbers = new String[dice];

                    int min = 1;
                    assert integerOption1 != null;
                    int max = integerOption1.getAsInt();
                    int range = (max - min) + 1;

                    numbers[counter] = ((int) (Math.random() * range) + min) + "   ";

                    list.append(numbers[counter]);
                }
                event.reply(String.valueOf(list)).queue();
            }
            case "help" -> event.reply("List of Commands\nhttps://github.com/jxshie/veryrarebot\n").queue();
            case "pfp" -> {
                OptionMapping userOption = event.getOption("user");
                if (userOption == null) {
                    String userAvatar = requireNonNull(event.getMember()).getUser().getAvatarUrl();
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
            case "flip" -> {
                int flip = (int) (Math.random() * 2);
                if (flip == 0) {
                    event.reply("Heads").queue();
                } else {
                    event.reply("Tails").queue();
                }
            }
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
        commandData.add(Commands.slash("help", "Sends a link to link of all the bot commands"));
        commandData.add(Commands.slash("ping", "Pong!"));
        commandData.add(Commands.slash("howdy", "Howdy!"));
        commandData.add(Commands.slash("flip", "Flips a coin"));


        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say.", true);
        OptionData option2 = new OptionData(OptionType.INTEGER, "max", "The maximum number of your roll(s).", true);
        OptionData option02 = new OptionData(OptionType.INTEGER, "dice", "How many dice you want to roll.", true);
        OptionData option3 = new OptionData(OptionType.USER, "user", "The user whose avatar you want.", false);
        commandData.add(Commands.slash("say", "Make the bot say a message.").addOptions(option1).setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        commandData.add(Commands.slash("roll", "Rolls a number between 1 and the max number you choose.").addOptions(option02, option2));
        commandData.add(Commands.slash("pfp", "Grab your own profile picture, or another persons.").addOptions(option3));


        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
