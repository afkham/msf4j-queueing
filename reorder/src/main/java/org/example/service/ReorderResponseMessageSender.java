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
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.example.service.ReorderServiceConstants.CF_NAME;
import static org.example.service.ReorderServiceConstants.CF_NAME_PREFIX;
import static org.example.service.ReorderServiceConstants.PASSWORD;
import static org.example.service.ReorderServiceConstants.QPID_ICF;
import static org.example.service.ReorderServiceConstants.QUEUE_NAME_PREFIX;
import static org.example.service.ReorderServiceConstants.REORDER_RESPONSE_QUEUE;
import static org.example.service.ReorderServiceConstants.USERNAME;
import static org.example.service.ReorderServiceConstants.getTCPConnectionURL;

/**
 * Sends responses to reorder requests.
 */
public class ReorderResponseMessageSender {

    private static QueueConnection queueConnection;
    private static QueueSession queueSession;

    public static void sendMessage(OrderResponse orderResponse) throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(USERNAME, PASSWORD));
        properties.put(QUEUE_NAME_PREFIX + REORDER_RESPONSE_QUEUE, REORDER_RESPONSE_QUEUE);
        InitialContext ctx = new InitialContext(properties);
        // Lookup connection factory
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
        queueConnection = connFactory.createQueueConnection();
        queueConnection.start();
        queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        // Send message
        Queue queue = (Queue) ctx.lookup(REORDER_RESPONSE_QUEUE);
        // create the message to send
        ObjectMessage message = queueSession.createObjectMessage(orderResponse);
        javax.jms.QueueSender queueSender = queueSession.createSender(queue);
        queueSender.send(message);
        queueSender.close();
        queueSession.close();
        queueConnection.close();
    }
}
