package com.jaredluo.rabbitmqlearning.workqueues;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Const.HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(Const.QUEUE_NAME, true, false, false, null);
		
		channel.basicQos(1);
		System.out.println("[*] waiting for message, to exit press ctrl+c");
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");

				try {
					doWork(message);
					System.out.println("[x] Received '" + message + "'");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("[x] Done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		boolean autoAck = false;
		channel.basicConsume(Const.QUEUE_NAME, autoAck, consumer);
	}

	protected static void doWork(String message) throws InterruptedException {
		for (char ch : message.toCharArray()) {
			if (ch == '.') {
				Thread.sleep(1000);
			}
		}
	}

}
