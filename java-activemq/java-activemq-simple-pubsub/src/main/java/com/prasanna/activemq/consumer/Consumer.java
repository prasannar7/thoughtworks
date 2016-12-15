package com.prasanna.activemq.consumer;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer implements Runnable{

	ActiveMQConnectionFactory activeMQConnectionFactory = null;
	
	public Consumer(ActiveMQConnectionFactory activeMQConnectionFactory) {
		this.activeMQConnectionFactory = activeMQConnectionFactory;
	}
	
	public void run() {
		try {
			Connection connection = activeMQConnectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination topic = session.createTopic("CLIMATE");
			MessageConsumer msgConsumer = session.createConsumer(topic);
			Message recievedMsg = msgConsumer.receive();
			TextMessage textMsg = (TextMessage) recievedMsg;
			System.out.println(textMsg.getText());
		} catch (JMSException e) {
			System.out.println("Excpetion : "+e.getMessage());
		}
	}

}
