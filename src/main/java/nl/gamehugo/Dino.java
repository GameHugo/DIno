package nl.gamehugo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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
        // Wait for the bot to be ready
        jda.awaitReady();

        // Add the listeners
        jda.addEventListener(new Roles());
        jda.addEventListener(new Talk());

        // Register the commands
        jda.updateCommands().addCommands(
                Commands.slash("talk", "Talk in the bot's name")
                        .addOption(OptionType.STRING, "message", "The content of the message", true)
                        .addOption(OptionType.CHANNEL, "channel", "The channel to send the message in", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        ).queue();

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
            }
        }, 0, 1000*5);
    }
}
