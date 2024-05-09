package nl.gamehugo;

import io.github.stefanbratanov.jvm.openai.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class AI extends ListenerAdapter {

    private final JDA jda = Dino.getJda();
    private final Map<Long, Long> cooldowns = new HashMap<>();
    @SuppressWarnings("FieldCanBeLocal")
    private final long COOLDOWN_TIME = 10;
    @SuppressWarnings("FieldCanBeLocal")
    private final String prompt = "You are a funny and intelligent bot." +
            " Your name is Dino." +
            " You can make jokes or be funny when questions are asked." +
            " Keep the answers short and simple." +
            " You can never use more that 50 words to answer a question." +
            " The message is never a compliment so don't say thanks." +
            " React in dutch, or english if you recognize it." +
            " React with everything i told you with this message: ";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        // check if message is a reply
        if (event.getMessage().getType() == MessageType.INLINE_REPLY) {
            if(event.getMessage().getReferencedMessage() == null) return;
            if (event.getMessage().getReferencedMessage().getAuthor().getIdLong() == jda.getSelfUser().getIdLong()) {
                event.getMessage().reply("I'm sorry, I can't reply to a reply.").queue();
            }
            return;
        }
        // check if message contains mention
        if (event.getMessage().getContentRaw().contains(jda.getSelfUser().getAsMention())) {
            long userId = event.getAuthor().getIdLong();
            String message = event.getMessage().getContentRaw().replace(jda.getSelfUser().getAsMention(), "");
            if (message.split(" ").length > 20) {
                event.getMessage().reply(
                        "I'm sorry, I can't process more than 20 words. Hugo would go bankrupt.").queue();
                return;
            }
            if (cooldowns.containsKey(userId) && System.currentTimeMillis() - cooldowns.get(userId) < (COOLDOWN_TIME * 1000)) {
                event.getMessage().reply("Please wait a few seconds before asking another question.").queue();
                return;
            }
            cooldowns.put(userId, System.currentTimeMillis());
            sendReply(prompt+message, event);
        }
    }

    public void sendReply(String message, MessageReceivedEvent event) {
        String response;
        try {
            OpenAI openAI = OpenAI.newBuilder(Dino.getOpenAIKey()).build();
            ChatClient chatClient = openAI.chatClient();
            CreateChatCompletionRequest createChatCompletionRequest = CreateChatCompletionRequest.newBuilder()
                    .model(OpenAIModel.GPT_3_5_TURBO)
                    .message(ChatMessage.userMessage(message))
                    .build();
            ChatCompletion chatCompletion = chatClient.createChatCompletion(createChatCompletionRequest);
            response = chatCompletion.choices().get(0).message().content();
        } catch (Exception e) {
            response = "I'm sorry, Something went wrong. Please try again later.\n" +
                    "If the problem persists, please contact a admin.";
            System.out.println(e.getMessage());
        }
        event.getMessage().reply(response).queue();
    }
}
