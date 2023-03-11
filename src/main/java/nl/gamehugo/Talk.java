package nl.gamehugo;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class Talk extends ListenerAdapter{

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("talk")) {
            event.deferReply(true).queue();
            if(event.getOption("message") == null) return;
            if(event.getOption("channel") == null) {
                event.getChannel().sendMessage(Objects.requireNonNull(event.getOption("message")).getAsString()).queue();
                event.getHook().sendMessage("Message send").queue();
                return;
            }
            if(!Objects.requireNonNull(event.getOption("channel")).getAsChannel().getType().isMessage()) {
                event.getHook().sendMessage("Channel is not a text channel").queue();
                return;
            }
            if(isNewsChannel(Objects.requireNonNull(event.getOption("channel")).getAsChannel())) {
                NewsChannel channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel().asNewsChannel();
                String message = Objects.requireNonNull(event.getOption("message")).getAsString();
                channel.sendMessage(message).queue();
                event.getHook().sendMessage("Message send in "+channel.getAsMention()).queue();
                return;
            } else if(isThreadChannel(Objects.requireNonNull(event.getOption("channel")).getAsChannel())) {
                ThreadChannel channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel().asThreadChannel();
                String message = Objects.requireNonNull(event.getOption("message")).getAsString();
                channel.sendMessage(message).queue();
                event.getHook().sendMessage("Message send in "+channel.getAsMention()).queue();
                return;
            }
            TextChannel channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel().asTextChannel();
            String message = Objects.requireNonNull(event.getOption("message")).getAsString();
            channel.sendMessage(message).queue();
            event.getHook().sendMessage("Message send in "+channel.getAsMention()).queue();
        }
    }

    private boolean isNewsChannel(Channel channel) {
        try {
            NewsChannel newsChannel = (NewsChannel) channel;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isThreadChannel(Channel channel) {
        try {
            ThreadChannel threadChannel = (ThreadChannel) channel;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
