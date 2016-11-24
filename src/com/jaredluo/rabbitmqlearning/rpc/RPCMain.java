package com.jaredluo.rabbitmqlearning.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

public class RPCMain {
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException,
			ConsumerCancelledException, InterruptedException {
		RPCClient fabonacciRPC = new RPCClient();
		System.out.println(" [x] Requesting fib(20)");
		String response = fabonacciRPC.call("20");
		System.out.println(" [.] Got '" + response + "'");
		fabonacciRPC.close();
	}
}
