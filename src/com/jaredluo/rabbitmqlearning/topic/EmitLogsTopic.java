package com.jaredluo.rabbitmqlearning.topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogsTopic {
	public static void main(String[] args) throws IOException, TimeoutException {
		while (true) {
			System.out.println("Please input log's topic: ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String msgSeverity = reader.readLine().trim();
			System.out.println("Please input log's detail: ");
			BufferedReader readerDetail = new BufferedReader(new InputStreamReader(System.in));
			String msgDetail = readerDetail.readLine().trim();

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(Const.TOPIC_EXCHANGE_NAME, "topic");
			channel.basicPublish(Const.TOPIC_EXCHANGE_NAME, msgSeverity, null, msgDetail.getBytes());

			System.out.println("[x] Sent logs with topic: " + msgSeverity + ", detail: " + msgDetail);
			channel.close();
			connection.close();
		}
	}
}
