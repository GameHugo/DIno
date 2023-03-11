package nl.gamehugo;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Talk extends ListenerAdapter{

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("talk")) {
            event.deferReply(true).queue();
            if(event.getOption("channel") == null || event.getOption("message") == null) return;
            if(!event.getOption("channel").getAsChannel().getType().isMessage()) {
                event.getHook().sendMessage("Channel is not a text channel").queue();
                return;
            }
            TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
            String message = event.getOption("message").getAsString();
            channel.sendMessage(message).queue();
            event.getHook().sendMessage("Message send in "+channel.getAsMention()).queue();
        }
    }

}
