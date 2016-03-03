/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.service;

import org.wso2.msf4j.MicroservicesRunner;

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.MessageConsumer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Application entry point.
 *
 * @since 0.1-SNAPSHOT
 */
public class Application {

    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String CF_NAME = "qpidConnectionfactory";
    private static String CARBON_CLIENT_ID = "carbon";
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static String CARBON_DEFAULT_HOSTNAME = "localhost";
    private static String CARBON_DEFAULT_PORT = "5672";

    private String userName = "admin";
    private String password = "admin";
    private String queueName = "testQueue";
    private QueueConnection queueConnection;
    private QueueSession queueSession;

    public static void main(String[] args) throws JMSException {
        Application queueReceiver = new Application();
        MessageConsumer consumer = null;
        try {
            consumer = queueReceiver.registerSubscriber();
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
            return;
        }

        try {
            while(true) {
                try {
                    queueReceiver.receiveMessages(consumer);
                } catch (NamingException | JMSException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } finally {
            // Housekeeping
            queueReceiver.cleanup(consumer);
        }

//        new MicroservicesRunner()
//                .deploy(new ReorderService())
//                .start();
    }

    private void cleanup(MessageConsumer consumer) throws JMSException {
        consumer.close();
        queueSession.close();
        queueConnection.stop();
        queueConnection.close();
    }

    public MessageConsumer registerSubscriber() throws NamingException, JMSException{
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
        properties.put("queue."+ queueName,queueName);
        InitialContext ctx = new InitialContext(properties);
        // Lookup connection factory
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
        queueConnection = connFactory.createQueueConnection();
        queueConnection.start();
        queueSession =
                queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        //Receive message
        Queue queue =  (Queue) ctx.lookup(queueName);
        MessageConsumer consumer = queueSession.createConsumer(queue);
        return consumer;
    }

    public void receiveMessages(MessageConsumer consumer) throws NamingException, JMSException {

        TextMessage message = (TextMessage) consumer.receive();
        System.out.println("Got message from queue receiver==>" + message.getText());


    }
    private String getTCPConnectionURL(String username, String password) {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return new StringBuffer()
                .append("amqp://").append(username).append(":").append(password)
                .append("@").append(CARBON_CLIENT_ID)
                .append("/").append(CARBON_VIRTUAL_HOST_NAME)
                .append("?brokerlist='tcp://").append(CARBON_DEFAULT_HOSTNAME).append(":").append(CARBON_DEFAULT_PORT).append("'")
                .toString();
    }
}
