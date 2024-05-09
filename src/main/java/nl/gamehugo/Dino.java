package nl.gamehugo;

import io.github.stefanbratanov.jvm.openai.OpenAI;
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
import nl.gamehugo.ai.AIChannelTalking;
import nl.gamehugo.ai.AITalking;
import nl.gamehugo.ai.Image;

import java.lang.Thread;
import java.util.*;

public class Dino {
    /**
     * Just a thank you to GitHub copilot for helping me for some parts of the code
     * <p>
     * Made by GameHugo
     */
    private static JDA jda;
    private static Dino instance;
    private static String openAIKey;

    public static void main(String[] args) throws InterruptedException {
        instance = new Dino();
        if (args.length == 0) {
            System.out.println("Please provide a token in the args");
            Thread.sleep(5000);
            return;
        }
        for (int i = 0; i < 50; ++i) System.out.println(" "); // just to clear the console and not leak the token
        System.out.println("Token provided starting DIno...");
        // Put the token in the first arg
        // Example: java -jar Dino.jar <token>
        if (args.length > 1) {
            openAIKey = args[1];
            System.out.println("OpenAI key provided");
        }
        jda = JDABuilder.createDefault(args[0])
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT, GatewayIntent.DIRECT_MESSAGES)
                .build();
        // Wait for the bot to be ready
        jda.awaitReady();

        // Add the listeners
        jda.addEventListener(new Talk());
        jda.addEventListener(new Rizz());
        jda.addEventListener(new Welcome());
        jda.addEventListener(new Clean());
        if (openAIKey != null) {
            jda.addEventListener(new AITalking());
            jda.addEventListener(new AIChannelTalking());
            jda.addEventListener(new Image());
        }

        // Register the commands
        jda.updateCommands().addCommands(
                Commands.slash("talk", "Talk in the bot's name")
                        .addOption(OptionType.STRING, "message", "The content of the message", true)
                        .addOption(OptionType.CHANNEL, "channel", "The channel to send the message in", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("clean", "Clean the chat")
                        .addOption(OptionType.INTEGER, "amount", "The amount of messages to delete", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)),
                Commands.context(Command.Type.USER, "Rizz it up"),
                Commands.slash("image", "Generate an image")
                        .addOption(OptionType.STRING, "image", "The prompt for the image", true)
        ).queue();

        // Set the status
        for (Guild guild : getJda().getGuilds())
            for (Member member : guild.getMembers())
                if (!member.getUser().isBot() && !members.contains(member))
                    members.add(member);
        status();

        System.out.println("Guilds: " + jda.getGuilds().size());
    }

    public static JDA getJda() {
        return jda;
    }

    public static List<Member> members = new ArrayList<>();
    private static boolean startedStatus = false;

    /**
     * Sets the status of the bot to watching a random member
     */
    private static void status() {
        if (startedStatus) return;
        startedStatus = true;

        Random random = new Random();
        final Member[] currentMember = new Member[1];
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    Member newMember = members.get(random.nextInt(members.size()));
                    if (!newMember.equals(currentMember[0])) {
                        currentMember[0] = newMember;
                        break;
                    }
                }
                getJda().getPresence().setActivity(Activity.watching("@" + currentMember[0].getEffectiveName() + "👀"));
            }
        }, 0, 1000 * 5);
    }

    public static Dino getInstance() {
        return instance;
    }

    private OpenAI openAI;
    public static OpenAI getOpenAI() {
        if(instance.openAI == null) {
            instance.openAI = OpenAI.newBuilder(openAIKey).build();
        }
        return instance.openAI;
    }
}
