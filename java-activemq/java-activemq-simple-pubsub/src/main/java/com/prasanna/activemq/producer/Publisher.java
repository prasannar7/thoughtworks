package com.prasanna.activemq.producer;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {

	private static Connection connection = null;
	private static Session session = null;
	private static MessageProducer messageProducer = null;
	
	public Publisher(ActiveMQConnectionFactory activeMQConnectionFactory) {
		createPublisher(activeMQConnectionFactory);
	}
	
	private void createPublisher(ActiveMQConnectionFactory activeMQConnectionFactory){
		try {
			connection =  activeMQConnectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination topic = session.createTopic("NEWS");
			messageProducer =  session.createProducer(topic);
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
		} catch (JMSException e) {
			System.out.println("Exception : "+e.getMessage());
		}
	}
	
	public static void sendMessage(String text){
		TextMessage textMessage;
		try {
			textMessage = session.createTextMessage(text);
			if("exit".equalsIgnoreCase(text)){
				messageProducer.send(textMessage);
				session.close();
				connection.close();
				System.exit(0);
			}
			messageProducer.send(textMessage);
		} catch (JMSException e) {
			System.out.println("Exception : "+e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		new Publisher(activeMQConnectionFactory);
		Scanner scanner = new Scanner(System.in);
		System.out.print("Started");
        while (true) {
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                sendMessage(input);
                break;
            }
            sendMessage(input);
        }
        scanner.close();
		sendMessage("sdf");
	}
}
