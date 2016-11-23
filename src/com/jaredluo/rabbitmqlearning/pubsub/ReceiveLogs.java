package com.jaredluo.rabbitmqlearning.pubsub;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ReceiveLogs {
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Const.HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(Const.EXCHANGE_NAME, "fanout");

		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, Const.EXCHANGE_NAME, "");

		System.out.println("[*] waiting for message, to exit press ctrl+c");
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("[x] Received '" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}
