package nl.gamehugo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Roles extends ListenerAdapter {
    TextChannel channel = Dino.getJda().getTextChannelById("1066677412752015430");
    HashMap<String, String> roles = new HashMap<>();

    public Roles() {
        HashMap<String, String> description = new HashMap<>();
        roles.put("gamer", "1055144057170567209");
        description.put("gamer", "Als je een echte gamer bent");
        roles.put("minecraft", "1055136360345907272");
        description.put("minecraft", "Als je een echte minecraft speler bent");
        roles.put("gezellig", "1057984229935427585");
        description.put("gezellig", "Als je echt gezellig bent (Deze rol kan gepind worden als iemand gezelligheid wilt)");
        if(channel == null) {
            System.out.println("Roles channel not found");
            return;
        }

        // Create the message
        CharSequence message = " ";

        // Create embed
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Roles");
        embed.setDescription("Klik op de knop om een rol te krijgen en nog een keer om de rol weg te halen");
        for(String role : roles.keySet())
            embed.addField("", "<@&"+roles.get(role)+">\n"+description.get(role), true);
        embed.setColor(0x00ff00);

        List<Button> buttons = new ArrayList<>();
        for(String role : roles.keySet())
            buttons.add(Button.primary(role.toLowerCase(), Objects.requireNonNull(Dino.getJda().getRoleById(roles.get(role))).getName()));

        // Create the buttons
        ItemComponent[] components = buttons.toArray(new ItemComponent[0]);

        // check the last 5 messages and check if the bot already send a message and if so, don't send a new one
        channel.getHistory().retrievePast(10).queue(messages -> {
            boolean edit = false;
            boolean found = false;
            for (Message historyMessage : messages) {
                if (!historyMessage.getAuthor().getId().equals(Dino.getJda().getSelfUser().getId())) continue;
                if(edit) {
                    historyMessage.delete().queue();
                    continue;
                }
                historyMessage.editMessage(message).setEmbeds(embed.build()).setActionRow(components).queue();
                edit = true;
                found = true;
            }
            if(!found) channel.sendMessage(message).setEmbeds(embed.build()).setActionRow(components).queue();
        });
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(event.getChannel().getIdLong() != channel.getIdLong()) return;
        event.deferEdit().queue();
        if(event.getButton().getId() == null || event.getGuild() == null || event.getMember() == null) return;
        if(!roles.containsKey(event.getButton().getId())) return;
        // check if member has the role already
        if(event.getMember().getRoles().contains(Objects.requireNonNull(event.getGuild().getRoleById(roles.get(event.getButton().getId()))))) {
            // take the role away
            event.getGuild().removeRoleFromMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById(roles.get(event.getButton().getId())))).queue();
            // send message
            event.getHook().setEphemeral(true).sendMessage("Je hebt de rol "+ Objects.requireNonNull(event.getGuild().getRoleById(roles.get(event.getButton().getId()))).getAsMention()+" weg gehaald").queue();
            return;
        }
        event.getGuild().addRoleToMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById(roles.get(event.getButton().getId())))).queue();
        // send message
        event.getHook().setEphemeral(true).sendMessage("Je hebt de rol "+ Objects.requireNonNull(event.getGuild().getRoleById(roles.get(event.getButton().getId()))).getAsMention()+" gekregen").queue();
    }
}
