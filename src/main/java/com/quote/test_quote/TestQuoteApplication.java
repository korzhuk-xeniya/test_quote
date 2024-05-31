package com.quote.test_quote;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.List;
import java.util.Random;

@SpringBootApplication
public class TestQuoteApplication {



    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        SpringApplication.run(TestQuoteApplication.class, args);


        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        Random random = new Random();
        GroupActor actor = new GroupActor(,
                "");
        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();
        while (true) {
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if(!messages.isEmpty()) {
                messages.forEach(message -> {
                    System.out.println(message.toString());
                    if(!message.getText().isEmpty()) {
                        try {
                            vk.messages().send(actor).message("Вы сказали: " +
                                    message.getText()).userId(message.getFromId()).randomId(random
                                    .nextInt(1000)).execute();
                        } catch (ApiException | ClientException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                        }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }

}
