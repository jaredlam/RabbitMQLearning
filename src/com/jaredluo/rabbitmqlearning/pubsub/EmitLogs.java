package com.jaredluo.rabbitmqlearning.pubsub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogs {
	public static void main(String[] args) throws IOException, TimeoutException {
		while (true) {
			System.out.println("Please input message:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input = reader.readLine();

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(Const.HOST);
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(Const.EXCHANGE_NAME, "fanout");

			channel.basicPublish(Const.EXCHANGE_NAME, "", null, input.getBytes());

			channel.close();
			connection.close();
		}
	}
}
