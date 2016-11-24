package com.jaredluo.rabbitmqlearning.rpc;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class RPCServer {

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Const.HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(Const.RPC_QUEUE_NAME, false, false, false, null);

		channel.basicQos(1);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(Const.RPC_QUEUE_NAME, false, consumer);

		while (true) {
			Delivery delivery = consumer.nextDelivery();
			BasicProperties basicProperties = delivery.getProperties();
			BasicProperties replyProperties = new BasicProperties.Builder()
					.correlationId(basicProperties.getCorrelationId()).build();

			String message = new String(delivery.getBody(), "UTF-8");
			int inputNum = Integer.parseInt(message);

			String response = "" + fib(inputNum);

			System.out.println("[.]fib(" + message + ") is " + response);
			channel.basicPublish("", basicProperties.getReplyTo(), replyProperties, response.getBytes());
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}

	}

	private static int fib(int n) throws Exception {
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;
		return fib(n - 1) + fib(n - 2);
	}
}
