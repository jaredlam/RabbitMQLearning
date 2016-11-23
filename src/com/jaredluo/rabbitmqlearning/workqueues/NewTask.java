package com.jaredluo.rabbitmqlearning.workqueues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.jaredluo.rabbitmqlearning.Const;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class NewTask {

	public static void main(String[] args) throws IOException, TimeoutException {
		while (true) {
			System.out.println("Please input message:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String message = br.readLine();
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(Const.HOST);
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(Const.QUEUE_NAME, false, false, false, null);
			// String message = getMsg(args);
			channel.basicPublish("", Const.QUEUE_NAME, null, message.getBytes());
			System.out.println("[x] Send '" + message + "'");

			channel.close();
			connection.close();
		}
	}

	private static String getMsg(String[] args) {
		if (args.length == 0) {
			return "Hello world";
		}
		return joinStrings(args, " ");
	}

	private static String joinStrings(String[] args, String delimiter) {
		if (args.length == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder(args[0]);
		for (int i = 1; i < args.length; i++) {
			result.append(delimiter).append(args[i]);
		}
		return result.toString();
	}
}
