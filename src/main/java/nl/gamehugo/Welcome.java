package nl.gamehugo;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Random;

public class Welcome extends ListenerAdapter {
    TextChannel channel = Dino.getJda().getTextChannelById("1055135994095087729");
    Role role = Dino.getJda().getRoleById("1055141032259498014");

    List<String> lines = List.of(
            "${user} Rawr! Welkom in de prehistorie van onze server!",
            "${user} Wees niet bang voor de dinos, ze bijten alleen als je niet hallo zegt.",
            "${user} Hoorde ik daar een brul? Oh wacht, dat was gewoon ons welkomstcomité.",
            "${user} Welkom bij Rawr, waar we praten als T-rexen en ons gedragen als velociraptors.",
            "${user} Voorzichtig, het kan zijn dat je hier af en toe een paar dinosaurus-botten tegenkomt.",
            "${user} Welkom bij de meest dino-tastische server die er is!",
            "${user} Kijk uit voor de stegosaurussen, die zijn vandaag een beetje humeurig.",
            "${user} Ben je klaar om te brullen en te grommen? Welkom bij Rawr!",
            "Rawr! ${user} is gearriveerd, welkom op onze server!",
            "Welkom bij Rawr, waar ${user} kan genieten van de prehistorische sfeer.",
            "Kijk eens wie er bij ons is gekomen, ${user}! Welkom bij onze dinosaurusfamilie.",
            "Bij Rawr maken we ons geen zorgen over de moderne wereld, hier draait alles om ${user} en de dinos.",
            "Laat je innerlijke dinosaurus los, ${user}! Welkom bij Rawr.",
            "Onze server is niet compleet zonder ${user}. Welkom bij Rawr!",
            "Wist je dat ${user} en dinosaurus in hetzelfde rijm passen? Welkom bij Rawr!",
            "Bij Rawr krijgt ${user} de kans om een T-rex te zijn, al is het maar voor een dag.",
            "Het is tijd om te brullen, ${user}! Welkom bij Rawr.",
            "Welkom bij Rawr, waar ${user} en dinosaurus hand in hand gaan.",
            "Dino's zijn onze vrienden, net als ${user}. Welkom bij Rawr!",
            "Bij Rawr zijn we er trots op dat we een dinosaurusvriendelijke server zijn, speciaal voor ${user}.",
            "Onze dinosaurusgemeenschap verwelkomt ${user} met open armen, welkom bij Rawr.",
            "We hebben een speciale plek voor ${user} gereserveerd in onze eigen Jurassic Park, welkom bij Rawr!",
            "Weet je wat onze server nodig heeft? ${user}! Welkom bij Rawr.",
            "Laat je tanden zien, ${user}! Welkom bij Rawr, waar het leven wild is.",
            "Onze server is het beste als ${user} deel uitmaakt van de actie. Welkom bij Rawr!",
            "Bij Rawr draait alles om dinos en ${user}. Welkom in onze prehistorische wereld!",
            "Voel je vrij om rond te lopen en te verkennen, ${user}. Welkom bij Rawr.",
            "We hebben een plek voor ${user} gereserveerd in onze dinosaurus-familie. Welkom bij Rawr!",
            "Ben je klaar om te reizen naar de tijd van de dinos, ${user}? Welkom bij Rawr!",
            "Bij Rawr ben jij net zo belangrijk als onze dinos. Welkom ${user}!",
            "Onze dinosaurusgemeenschap kan niet wachten om ${user} te verwelkomen, welkom bij Rawr!",
            "Bij Rawr zijn we dol op alles wat met dinos te maken heeft, inclusief ${user}. Welkom bij onze server!",
            "Hier bij Rawr zijn we allemaal een beetje dino-gek, net als ${user}. Welkom bij onze prehistorische wereld!"
    );

    Random random = new Random();
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if(channel == null) return;
        if(role == null) {
            channel.sendMessage(event.getMember().getAsMention() + " je hebt nog geen role gekregen... RIP JOU LOL😝").queue();
            channel.sendMessage("L LOSER🤣😂🤣").queue();
        } else {
            event.getGuild().addRoleToMember(event.getMember(), role).queue();
        }
        String message = lines.get(random.nextInt(lines.size())).replace("${user}", event.getMember().getAsMention());
        channel.sendMessage(message).queue();
    }
}
