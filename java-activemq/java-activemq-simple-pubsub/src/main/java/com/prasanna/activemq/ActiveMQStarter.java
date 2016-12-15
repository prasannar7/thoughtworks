package com.prasanna.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.prasanna.activemq.consumer.Consumer;
import com.prasanna.activemq.producer.Producer;

public class ActiveMQStarter {
    public static void main(String[] args) throws InterruptedException{
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Thread consumerThread = new Thread(new Consumer(activeMQConnectionFactory));
        consumerThread.start();
        Thread.sleep(1000);
        Thread producerThread = new Thread(new Producer(activeMQConnectionFactory));
        producerThread.start();
    }
}
