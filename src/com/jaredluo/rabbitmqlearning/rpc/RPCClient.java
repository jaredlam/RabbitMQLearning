package com.jaredluo.rabbitmqlearning.rpc;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class RPCClient {
	private Connection connection;
	private Channel channel;
	private QueueingConsumer replyQueueConsumer;
	private String replyQueue;

	public RPCClient() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Const.HOST);
		connection = factory.newConnection();
		channel = connection.createChannel();

		replyQueueConsumer = new QueueingConsumer(channel);
		replyQueue = channel.queueDeclare().getQueue();
		channel.basicConsume(replyQueue, true, replyQueueConsumer);
	}

	public String call(String msg)
			throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		String correlationId = UUID.randomUUID().toString();
		BasicProperties basicProperties = new BasicProperties.Builder().replyTo(replyQueue).correlationId(correlationId)
				.build();

		channel.basicPublish("", Const.RPC_QUEUE_NAME, basicProperties, msg.getBytes());

		String response = null;

		while (true) {
			Delivery delivery = replyQueueConsumer.nextDelivery();
			if (correlationId.equals(delivery.getProperties().getCorrelationId())) {
				response = new String(delivery.getBody(), "UTF-8");
				break;
			}
		}
		return response;
	}

	public void close() throws IOException {
		connection.close();
	}
}
