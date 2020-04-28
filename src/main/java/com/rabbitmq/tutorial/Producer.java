package com.rabbitmq.tutorial;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.impl.ChannelN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer
{
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
            channel.queueDeclare("test-queue", true, false, false, null);

            Scanner scanner = new Scanner(System.in);

            while(true)
            {
                logger.info("Enter your message: ");
                String message = scanner.nextLine();
                channel.basicPublish("test-fanout-exchange", "test", null , message.getBytes());
            }
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
        }
        finally {
            if(channel != null) channel.close();
            if(connection != null) connection.close();
        }
    }
}