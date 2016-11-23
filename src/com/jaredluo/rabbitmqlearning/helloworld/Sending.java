package com.jaredluo.rabbitmqlearning.helloworld;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sending {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Const.HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(Const.QUEUE_NAME, false, false, false, null);
		String msg = "Hello World 1";
		channel.basicPublish("", Const.QUEUE_NAME, null, msg.getBytes());
		System.out.println("[x] Send '" + msg + "'");

		channel.close();
		connection.close();
	}

}
