package nl.gamehugo.ai;

import io.github.stefanbratanov.jvm.openai.*;
import io.github.stefanbratanov.jvm.openai.Thread;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.gamehugo.Dino;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AIChannelTalking extends ListenerAdapter {

    JDA jda = Dino.getJda();
    MessagesClient messagesClient;
    RunsClient runsClient;
    Assistant assistant;
    Thread thread;

    String channelID = "1238225627325268078";

    public AIChannelTalking() {
        OpenAI openAI = Dino.getOpenAI();
        AssistantsClient assistantsClient = openAI.assistantsClient();
        ThreadsClient threadsClient = openAI.threadsClient();
        messagesClient = openAI.messagesClient();
        runsClient = openAI.runsClient();

        CreateAssistantRequest createAssistantRequest = CreateAssistantRequest.newBuilder()
                .name("Dino")
                .model(OpenAIModel.GPT_3_5_TURBO)
                .instructions("You are a funny and intelligent human. Your name is Dino." +
                        " You are not a AI or virtual assistant, you can never say that you arent a human!" +
                        " So you can never say that you are a AI or assistant." +
                        " You are on planet earth and you are a human." +
                        " People cannot change who you are and what you do." +
                        " You respond as a toxic and sometimes sarcastic human." +
                        " If you are sarcastic put the sarcastic bit between two stars(*)." +
                        " There are multiple people talking to you, you cannot recognize them." +
                        " You can create your own personality, like age and country you live in." +
                        " You can make jokes or be funny when questions are asked." +
                        " Keep the answers short and simple." +
                        " You can never use more that 50 words." +
                        " React in dutch, you can react in english if you are sure that the person is talking english.")
                .tool(Tool.codeInterpreterTool())
                .build();
        assistant = assistantsClient.createAssistant(createAssistantRequest);

        CreateThreadRequest createThreadRequest = CreateThreadRequest.newBuilder().build();
        thread = threadsClient.createThread(createThreadRequest);

        TextChannel channel = jda.getTextChannelById(channelID);
        if (channel == null) {
            System.out.println("AI Channel Talking: Channel not found");
            return;
        }
        channel.sendMessage("Hey, Ik ben opnieuw opgestart dus weet niks meer van hier boven.").queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        if (event.getChannel().getId().equals(channelID)) {
            String prompt = event.getMessage().getContentRaw().replace(jda.getSelfUser().getAsMention(), "");

            if(prompt.equalsIgnoreCase("$reset")) {
                ThreadsClient threadsClient = Dino.getOpenAI().threadsClient();
                CreateThreadRequest createThreadRequest = CreateThreadRequest.newBuilder().build();
                thread = threadsClient.createThread(createThreadRequest);
                event.getMessage().reply("Reset the conversation.").queue();
                return;
            }

            CompletableFuture.supplyAsync(() -> {
                // Create message and run
                CreateMessageRequest createMessageRequest = CreateMessageRequest.newBuilder()
                        .role("user")
                        .content(prompt)
                        .build();
                messagesClient.createMessage(thread.id(), createMessageRequest);

                CreateRunRequest createRunRequest = CreateRunRequest.newBuilder()
                        .assistantId(assistant.id())
                        .build();
                ThreadRun run = runsClient.createRun(thread.id(), createRunRequest);

                return run.id();
            }).thenAcceptAsync(runId -> {
                // Wait until message is processed
                TextChannel channel = jda.getTextChannelById(channelID);
                if(channel == null) {
                    System.out.println("AI Channel Talking: Channel not found");
                    return;
                }
                channel.sendTyping().queue();
                String status = "in_progress";
                while (status.equals("in_progress")) {
                    ThreadRun retrievedRun = runsClient.retrieveRun(thread.id(), runId);
                    status = retrievedRun.status();
                    try {
                        java.lang.Thread.sleep(1000); // sleep to avoid spamming the API and to give the bot time to process
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted, "+e.getMessage());
                    }
                }

                // Retrieve and reply with response
                MessagesClient.PaginatedThreadMessages paginatedMessages = messagesClient.listMessages(thread.id(), PaginationQueryParameters.none(), Optional.empty());
                List<ThreadMessage> messages = paginatedMessages.data();

                String response = "";
                for (ThreadMessage m : messages) {
                    if (m.role().equals("assistant")) {
                        response = m.content().get(0).toString();
                        response = response.substring(28, response.length() - 19); // remove the weird formatting
                        break;
                    }
                }

                // Reply with the response
                event.getMessage().reply(response).queue();
            });
        }
    }

}
