package com.springdata.jpa;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducer {
	private final StreamBridge streamBridge;

	public KafkaMessageProducer(StreamBridge streamBridge) {
		this.streamBridge = streamBridge; // Dependency is injected here
	}

	public void sendMessage(String message) {
		streamBridge.send("outputChannel", MessageBuilder.withPayload(message).build());
		System.out.println("Message sent: " + message);
	}
}
