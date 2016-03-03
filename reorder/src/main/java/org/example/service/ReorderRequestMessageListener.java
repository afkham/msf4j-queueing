/*
 *  Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.example.service;

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * TODO: class level comment
 */
public class ReorderRequestMessageListener implements MessageListener {

    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String CF_NAME = "qpidConnectionfactory";
    private static String CARBON_CLIENT_ID = "carbon";
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static String CARBON_DEFAULT_HOSTNAME = "localhost";
    private static String CARBON_DEFAULT_PORT = "5672";

    private String userName = "admin";
    private String password = "admin";
    private String reorderRequestQueue = "reorderRequestQueue";
    private QueueConnection queueConnection;
    private QueueSession queueSession;

    public ReorderRequestMessageListener() {
        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
            properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
            properties.put("queue." + reorderRequestQueue, reorderRequestQueue);
            InitialContext ctx = new InitialContext(properties);
            // Lookup connection factory
            QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
            queueConnection = connFactory.createQueueConnection();
            queueConnection.start();
            queueSession =
                    queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            //Receive message
            Queue queue = (Queue) ctx.lookup(reorderRequestQueue);
            MessageConsumer consumer = queueSession.createConsumer(queue);
            consumer.setMessageListener(this);
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage msg = (ObjectMessage) message;
        try {
            Order order = (Order) msg.getObject();
            System.out.println("Got message from queue receiver==>" + order);
            //TODO send response

            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setItemCode(order.getItemCode());
            orderResponse.setOrderQuantity(order.getQuantity());

            ReorderResponseMessageSender.sendMessage(orderResponse);
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }

    private String getTCPConnectionURL(String username, String password) {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return "amqp://"+ username + ":" + password + "@" + CARBON_CLIENT_ID+ "/" + CARBON_VIRTUAL_HOST_NAME +
                "?brokerlist='tcp://" + CARBON_DEFAULT_HOSTNAME + ":" + CARBON_DEFAULT_PORT + "'";
    }
}
