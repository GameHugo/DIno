package nl.gamehugo;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class Clean extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!event.getName().equals("clean")) return;
        event.deferReply(true).queue();
        // get the amount of messages to delete
        if(event.getOption("amount") == null) return;
        if(!isInteger(Objects.requireNonNull(event.getOption("amount")).getAsString())) {
            event.getHook().sendMessage("Amount is not a number").queue();
            return;
        }
        int amount = Integer.parseInt(Objects.requireNonNull(event.getOption("amount")).getAsString());
        if(amount > 100) {
            event.getHook().sendMessage("Amount is too high (Limit is 100)").queue();
            return;
        }
        // delete the messages
        try {
            event.getChannel().getIterableHistory().takeAsync(amount).thenAcceptAsync(messages -> {
                event.getChannel().purgeMessages(messages);
                event.getHook().sendMessage("Deleted "+amount+" messages").queue();
            });
        } catch (Exception e) {
            event.getHook().sendMessage("Something went wrong :(\nTry a smaller number?").queue();
        }
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
