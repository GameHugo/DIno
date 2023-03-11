package nl.gamehugo;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Rizz extends ListenerAdapter {

    List<String> rizzlers = List.of(
            "${user} je handen zien er zwaar uit😪 mag ik ze voor je vast houden😏",
            "${user} kan je reanimatie🥵 want je hebt zojuist mijn adem weg gehaald😮‍💨",
            "${user} ik heb een vraagje🤔 mag ik je handen vast houden🧑‍🤝‍🧑",
            "${user} You. Me. Gas station. What are we getting for dinner? Sushi of course. Uh oh! There was a roofie in our gas station sushi. We black out and wake up in a sewer surrounded by fish. Horny fish. You know what that means. Fish orgy. The stench draws in a bear. What do we do? We're gonna fight it. Bear fight. Bare handed. Bare, naked? Oh, yes please. We befriend the bear after we beat it in a brawl and ride it into a Chuck E. Cheese. Dance Dance Revolution. Revolution? Overthrow the government? Uh, I think so. Next thing you know, I'm reincarnated as Jesus Christ. Then I turn into a jet, fly into the sun, black out again, wake up, do a bump, white out, which I didn't even know you could do. Then I smoked a joint, greened out. Then I turn into the sun. Uh oh! Looks like the meth is kicking in. aklfhaofhasfahfakh AAAAAAAAA afahfioahflkf AAAAA https://www.youtube.com/watch?v=LaGP-CVfENQ",
            "${user} ik ben nieuw in de buurt🏘️ kan je me de richting geven naar je huis😽",
            "${user} als je nu met me mee komt🚶 zou ik je vanavond laten zien waarom ze mij spiderman noemen🕸️",
            "${user}, ben je een magiër? Want telkens als ik naar je kijk, verdwijnt alles om me heen en blijf jij als enige over. 🧙‍♂️✨😍",
            "${user}, je ogen zijn zo helder als de sterrenhemel. Ik kan er eindeloos in verdwalen. 👀💫✨",
            "${user}, is er iets mis met mijn ogen? Want ik kan mijn blik niet van je afhouden. 👀😏😉",
            "${user}, ben je toevallig een genie? Want ik kan niet geloven hoe aantrekkelijk je bent. 👌💡🤩",
            "${user}, je bent als een verslavende geur die ik niet kan weerstaan. Ik wil blijven ruiken en je beter leren kennen. 👃❤️👀",
            "${user}, wil je mijn medicijn zijn? Ik voel me zo ziek van jouw schoonheid. 😍🤒💊",
            "${user}, als ik naar jou kijk, voel ik de vlinders in mijn buik fladderen en weet ik dat ik de ware heb gevonden. 👀❤️🦋",
            "${user}, ik zou graag mijn tijd met je willen delen en je beter leren kennen, want ik denk dat er iets speciaals tussen ons kan ontstaan. 💑💬❤️",
            "${user}, jij bent het zonnetje in mijn leven. Ik wil je elke dag zien stralen. 😍☀️😎",
            "${user}, je bent zo adembenemend mooi dat ik bijna mijn adem inhoud als ik je zie. 👌😮‍💨😍",
            "${user}, je bent als een sterrenhemel op een heldere nacht, vol met schitterende lichtpuntjes die mijn aandacht trekken. ✨🌟😍",
            "${user}, als liefde een taal was, dan zou ik je elke dag willen vertellen hoeveel ik van je hou. ❤️💬🌹",
            "${user}, je hebt een glimlach die de wereld doet stoppen met draaien en mijn hart sneller laat kloppen. 😍💕😊",
            "${user}, ik weet niet of het door je ogen komt of door je lach, maar je maakt me zo gelukkig. 😊💖👀",
            "${user}, als ik aan je denk, voel ik een warme gloed in mijn hart die me vertelt dat ik de juiste persoon heb gevonden. ❤️🔥😌",
            "${user}, ik wil graag in je ogen verdrinken en me verliezen in je ziel. 👀💕😍",
            "${user}, als ik jou zie, voel ik me als een kind in een snoepwinkel. Ik wil alles van je proeven. 🍬😜😘",
            "${user}, ik kan niet stoppen met aan je te denken. Zou je mij willen helpen om mijn gedachten op iets anders te richten? 😏🤔😉",
            "${user}, als ik bij jou in de buurt ben, voel ik me als een vogel die voor het eerst zijn vleugels uitslaat en de vrijheid ontdekt. 🕊️💖😍",
            "${user}, ik geloof dat er een reden is waarom we elkaar hebben ontmoet. Misschien kunnen we samen iets geweldigs beleven. 💫🤞❤️"
    );

    Random random = new Random();
    List<Integer> used = new ArrayList<>();
    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        if (event.getName().equals("Rizz it up")) {
            event.deferReply(false).queue();
            int randomLine = 0;
            for (int i = 0; i < 250; i++) {
                if(used.size() >= rizzlers.size()) used.clear();
                int newRandom = random.nextInt(rizzlers.size());
                if (used.contains(newRandom)) continue;
                used.add(newRandom);
                randomLine = newRandom;
                break;
            }
            String message = rizzlers.get(randomLine).replace("${user}", Objects.requireNonNull(event.getTargetMember()).getAsMention());
            event.getHook().sendMessage(message).queue();
        }
    }

}
