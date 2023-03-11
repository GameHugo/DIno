package nl.gamehugo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.IOException;
import java.util.*;

public class Dino {
    /**
      Just a thank you to GitHub copilot for helping me for some parts of the code

      Made by GameHugo
     */
    private static JDA jda;
    public static void main(String[] args) throws InterruptedException, IOException {
        if(args.length == 0) {
            System.out.println("Please provide a token in the args");
            return;
        }
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
        jda.addEventListener(new Rizz());
        jda.addEventListener(new Welcome());

        new Qod();

        // Register the commands
        jda.updateCommands().addCommands(
                Commands.slash("talk", "Talk in the bot's name")
                        .addOption(OptionType.STRING, "message", "The content of the message", true)
                        .addOption(OptionType.CHANNEL, "channel", "The channel to send the message in", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.context(Command.Type.USER, "Rizz it up")
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
        final Member[] member = new Member[1];
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    Member newMember = members.get(random.nextInt(members.size()));
                    if(!newMember.equals(member[0])) {
                        member[0] = newMember;
                        break;
                    }
                }
                getJda().getPresence().setActivity(Activity.watching("@"+member[0].getEffectiveName()+"👀"));
            }
        }, 0, 1000*5);
    }
}
