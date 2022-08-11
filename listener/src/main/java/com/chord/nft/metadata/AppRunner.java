package com.chord.nft.metadata;

import com.chord.nft.metadata.message.RabbitMQSender;
import com.chord.nft.metadata.service.IndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class AppRunner {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${app.run}")
    private boolean appRunMode;


    @Autowired
    IndexerService indexerService;

    @Autowired
    RabbitMQSender rabbitMQSender;

    public void run() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
			/*
            Setting auto commit false to make it one logical unit of work.
            Either all gets saved or none if process gets hanged or killed.
        	*/
            conn.setAutoCommit(false);

            while(appRunMode){
                // add logic to sleep if latest block already indexed.
                indexerService.indexBlocks(conn);
            }
        } catch (Exception e) {
            System.err.println("Process has encountered exception hence terminating it.");
            e.printStackTrace();

            if (e instanceof SQLException) {
                System.err.format("SQL State: %s\n%s", ((SQLException) e).getSQLState(), e.getMessage());
            }

            // Most likely reason lost DB connection, hence kill the process so that a new instance will replace it from healthcheck.
            System.exit(0);
        }
    }

}
