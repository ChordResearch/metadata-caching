package com.chord.nft.metadata.message;


import com.chord.nft.metadata.model.Token;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    public void send(Token token) {
        rabbitTemplate.convertAndSend(exchange, routingkey, token);
        System.out.println("Send msg = " + token);

    }
}

