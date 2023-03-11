package nl.gamehugo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Qod {
    TextChannel channel = Dino.getJda().getTextChannelById("1084247182577770566");

    public Qod() throws IOException {
        if(channel == null) {
            System.out.println("Qod channel not found");
            return;
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                channel.getHistory().retrievePast(5).queue(messages -> {
                    boolean foundOld = false;
                    boolean sendNew = false;
                    for (Message historyMessage : messages) {
                        if(!historyMessage.getAuthor().getId().equals(Dino.getJda().getSelfUser().getId())) continue;
                        foundOld = true;
                        Calendar calendar = Calendar.getInstance();
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        if(!(historyMessage.getTimeCreated().getDayOfMonth()+1 < dayOfMonth)) continue;
                        try {
                            channel.sendMessageEmbeds(newQuote().build()).queue();
                            sendNew = true;
                        } catch (IOException e) {
                            channel.sendMessage("Er is een error! SORRYYY").queue();
                            System.out.println("Qod Error: "+e.getMessage());
                            sendNew = true;
                        }
                    }
                    if(!foundOld && !sendNew) {
                        try {
                            channel.sendMessageEmbeds(newQuote().build()).queue();
                        } catch (IOException e) {
                            channel.sendMessage("Er is een error! SORRYYY").queue();
                            System.out.println("Qod Error: "+e.getMessage());
                        }
                    }
                });
            }
        }, 0, 1000*60*10);
    }

    private EmbedBuilder newQuote() throws IOException {
        URL url = new URL("https://quotes.rest/qod");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseStream);
        System.out.println(root.path("contents").path("quotes").path(0).path("title").asText());
        System.out.println(root.path("contents").path("quotes").path(0).path("quote").asText());
        System.out.println(root.path("contents").path("quotes").path(0).path("author").asText());
        String title = root.path("contents").path("quotes").path(0).path("title").asText();
        String quote = root.path("contents").path("quotes").path(0).path("quote").asText();
        String author = root.path("contents").path("quotes").path(0).path("author").asText();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(quote);
        embedBuilder.setFooter(author);
        embedBuilder.setTimestamp(new Date().toInstant());
        embedBuilder.setColor(0x00ff00);
        return embedBuilder;
    }

}
