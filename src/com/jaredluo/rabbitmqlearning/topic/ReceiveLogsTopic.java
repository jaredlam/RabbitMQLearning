package com.jaredluo.rabbitmqlearning.topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogsTopic {
	public static void main(String[] args) throws IOException, TimeoutException {
		System.out.println("which topic's log you want subscribe?:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String severities = reader.readLine();
		String[] severityArray = splitSeverity(severities);

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(Const.TOPIC_EXCHANGE_NAME, "topic");
		String queueName = channel.queueDeclare().getQueue();

		for (String severity : severityArray) {
			channel.queueBind(queueName, Const.TOPIC_EXCHANGE_NAME, severity.trim());
		}

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("[x] Received '" + message + "'");
			};
		};

		channel.basicConsume(queueName, true, consumer);
	}

	private static String[] splitSeverity(String severities) {
		if (severities.indexOf(",") == -1) {
			return new String[] { severities };
		}
		return severities.split(",");
	}
}
