package com.chord.nft.metadata.consumer;

import com.chord.nft.metadata.model.Token;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQConsumer {
    @RabbitListener(queues = "${rabbitmq.queue}")
    public void recievedMessage(Token token) {
        System.out.println("Recieved Message From RabbitMQ: " + token);
    }
}