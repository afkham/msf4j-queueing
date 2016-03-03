/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.example.service;

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReorderRequestMessageSender {

    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String QUEUE_NAME_PREFIX = "queue.";
    private static final String CF_NAME = "qpidConnectionfactory";

    private static final String CARBON_CLIENT_ID = "carbon";
    private static final String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static final String CARBON_DEFAULT_HOSTNAME = "localhost";
    private static final String CARBON_DEFAULT_PORT = "5672";

    private static String userName = "admin";
    private static String password = "admin";

    private static String queueName = "reorderRequestQueue";
//    private static String queueName = "testQueue";
    private static QueueConnection queueConnection;
    private static QueueSession queueSession;

    public static void sendMessage(Order order) throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
        properties.put(QUEUE_NAME_PREFIX + queueName, queueName);
        InitialContext ctx = new InitialContext(properties);
        // Lookup connection factory
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
        queueConnection = connFactory.createQueueConnection();
        queueConnection.start();
        queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        // Send message
        Queue queue = (Queue) ctx.lookup(queueName);
        // create the message to send
        ObjectMessage message = queueSession.createObjectMessage(order);
        javax.jms.QueueSender queueSender = queueSession.createSender(queue);
        queueSender.send(message);
        queueSender.close();
        queueSession.close();
        queueConnection.close();
    }

    private static String getTCPConnectionURL(String username, String password) {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return "amqp://" + username + ":" + password + "@" + CARBON_CLIENT_ID + "/" + CARBON_VIRTUAL_HOST_NAME +
                "?brokerlist='tcp://" + CARBON_DEFAULT_HOSTNAME + ":" + CARBON_DEFAULT_PORT + "'";
    }

}
