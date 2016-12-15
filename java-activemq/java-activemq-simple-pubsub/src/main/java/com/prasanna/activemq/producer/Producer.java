package com.prasanna.activemq.producer;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer implements Runnable{

	//Connection Factory which will help in connecting to ActiveMQ serer
	ActiveMQConnectionFactory activeMQConnectionFactory = null;
	
	public Producer(ActiveMQConnectionFactory activeMQConnectionFactory) {
		this.activeMQConnectionFactory = activeMQConnectionFactory;
	}
	
	public void run() {
		try {
			// First create a connection
			Connection connection = activeMQConnectionFactory.createConnection();
			connection.start();
			// Now create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// Let's create a topic. If the topic exist, it will return that
			Destination topic = session.createTopic("CLIMATE");
			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer msgProducer = session.createProducer(topic);
			msgProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			// Create a messages for the current climate
			String text = "Today climate is hot!";
			TextMessage textMsg = session.createTextMessage(text);
			// Send the message to topic
			msgProducer.send(textMsg);
			// Do the cleanup
			session.close();
			connection.close();
		} catch (JMSException e) {
			System.out.println("Exception : "+e.getMessage());
		}
	}

}
