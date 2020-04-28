package com.rabbitmq.tutorial;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer
{
    Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main( String[] args ) throws IOException, TimeoutException
    {
        Logger logger = LoggerFactory.getLogger(Producer.class);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("password");
        Connection connection = null;
        Channel channel = null;
        try
        {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare("test-fanout-exchange", "fanout", true);
            channel.queueDeclare("test-queue", true, false, false, null);
            String uniqueID = UUID.randomUUID().toString();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info(" [x] Received '" + consumerTag + "':'" + message + "'");
            };

            channel.basicConsume("test-queue", true, uniqueID,deliverCallback, consumerTag -> { });
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
        }
    }
}