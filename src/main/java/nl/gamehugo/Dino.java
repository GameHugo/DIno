package nl.gamehugo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.*;

public class Dino {
    private static JDA jda;
    public static void main(String[] args) throws InterruptedException {
        // Put the token in the first arg
        // Example: java -jar Dino.jar <token>
        jda = JDABuilder.createDefault(args[0])
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        // optionally block until JDA is ready
        jda.awaitReady();

        // Set the status
        status();

        System.out.println("Guilds: "+jda.getGuilds().size());
    }

    public static JDA getJda() {
        return jda;
    }

    private static boolean status = false;
    private static void status() {
        if(status) return;
        status = true;
        List<Member> members = new ArrayList<>();
        for(Guild guild : getJda().getGuilds())
            for(Member member : guild.getMembers())
                if(!member.getUser().isBot() && !members.contains(member))
                    members.add(member);
        Random random = new Random();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                getJda().getPresence().setActivity(Activity.watching("@"+members.get(random.nextInt(members.size())).getEffectiveName()+"👀"));
                System.out.println("Status changed");
            }
        }, 0, 1000*5);
    }
}
