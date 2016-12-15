package com.prasanna.activemq.consumer;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Reciever implements MessageListener{

	Connection connection = null;
	Session session = null;
	
	public Reciever(ActiveMQConnectionFactory activeMQConnectionFactory) {
		createProducer(activeMQConnectionFactory);
	}
	
	private void createProducer(ActiveMQConnectionFactory activeMQConnectionFactory){
		try {
			connection =  activeMQConnectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination topic =  session.createTopic("NEWS");
			MessageConsumer msgConsumer =  session.createConsumer(topic);
			msgConsumer.setMessageListener(this);
		} catch (JMSException e) {
			System.out.println("Exception : "+e.getMessage());
		}
	}
	
	public void onMessage(Message message) {
		TextMessage textMsg = (TextMessage) message;
		try {
			if("exit".equalsIgnoreCase(textMsg.getText())){
				session.close();
				connection.close();
				System.exit(0);
			}
			System.out.println(textMsg.getText());
		} catch (JMSException e) {
			System.out.println("Exception : "+e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		new Reciever(activeMQConnectionFactory);
	}

}
