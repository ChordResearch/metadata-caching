package com.chord.nft.metadata;

import com.chord.nft.metadata.model.Token;
import com.chord.nft.metadata.sender.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
//@ComponentScan(excludeFilters =
//        {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RabbitTemplateConfiguration.class)})
public class MetadataConsumerApplication  implements CommandLineRunner {

	@Autowired
	RabbitMQSender rabbitMQSender;

	public static void main(String[] args) {
		SpringApplication.run(MetadataConsumerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Token token = new Token();
		token.setTokenId("testtokenid");
		token.setNftContractAddress("testnftcontractaddress");

		rabbitMQSender.send(token);
	}
}
