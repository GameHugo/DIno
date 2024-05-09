package nl.gamehugo.ai;

import io.github.stefanbratanov.jvm.openai.CreateImageRequest;
import io.github.stefanbratanov.jvm.openai.Images;
import io.github.stefanbratanov.jvm.openai.ImagesClient;
import io.github.stefanbratanov.jvm.openai.OpenAI;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import nl.gamehugo.Dino;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Image extends ListenerAdapter {

    private final Map<Long, Long> cooldowns = new HashMap<>();
    @SuppressWarnings("FieldCanBeLocal")
    private final long COOLDOWN_TIME = 2*60;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!event.getName().equals("image")) return;
        if(event.getOption("image") == null) return;
        long userId = event.getUser().getIdLong();
        if (cooldowns.containsKey(userId) && System.currentTimeMillis() - cooldowns.get(userId) < (COOLDOWN_TIME * 1000)) {
            event.deferReply(true).queue();
            event.getHook().sendMessage("You need to wait "+
                    (COOLDOWN_TIME - (System.currentTimeMillis() - cooldowns.get(userId))/1000)+
                    " seconds before using this command again\n" +
                    "Images cost a lot of money to generate, so I need to limit the amount of images generated").queue();
            return;
        }
        event.deferReply().queue();
        cooldowns.put(event.getUser().getIdLong(), System.currentTimeMillis());
        String image = Objects.requireNonNull(event.getOption("image")).getAsString();
        try {
            OpenAI openAI = OpenAI.newBuilder(Dino.getOpenAIKey()).build();
            ImagesClient imagesClient = openAI.imagesClient();
            CreateImageRequest createImageRequest = CreateImageRequest.newBuilder()
                    .model("dall-e-3")
                    .prompt(image)
                    .build();
            Images images = imagesClient.createImage(createImageRequest);
            String url = images.data().get(0).url();
            String prompt = images.data().get(0).revisedPrompt();
            if(prompt == null) prompt = image;
            File file = new File("image.png");
            try {
                BufferedImage bufferedImage = ImageIO.read(new URL(url));
                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException e) {
                event.getHook().sendMessage("Something went wrong writing the image\nURL: "+url).queue();
                return;
            }
            FileUpload fileUpload = FileUpload.fromData(file, "image.png");
            event.getHook().sendMessage(prompt).addFiles(fileUpload).queue();
        } catch (Exception e) {
            if(e.getMessage().contains("safety system")) {
                event.getHook().sendMessage("The prompt is not safe to generate an image with").queue();
            } else {
                event.getHook().sendMessage("Something went wrong... try again.\n" +
                        "If the problem persists, please contact a admin.").queue();
            }
        }
    }

}
