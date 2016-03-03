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

/**
 * Constants.
 */
public class PurchasingServiceConstants {
    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    public static final String CF_NAME_PREFIX = "connectionfactory.";
    public static final String QUEUE_NAME_PREFIX = "queue.";
    public static final String CF_NAME = "qpidConnectionfactory";

    public static final String CARBON_CLIENT_ID = "carbon";
    public static final String CARBON_VIRTUAL_HOST_NAME = "carbon";
    public static final String CARBON_DEFAULT_HOSTNAME = "localhost";
    public static final String CARBON_DEFAULT_PORT = "5672";

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";

    public static String REORDER_REQUEST_QUEUE = "reorderRequestQueue";
    public static String REORDER_RESPONSE_QUEUE = "reorderResponseQueue";

    public static String getTCPConnectionURL(String username, String password) {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return "amqp://" + username + ":" + password + "@" + CARBON_CLIENT_ID + "/" + CARBON_VIRTUAL_HOST_NAME +
                "?brokerlist='tcp://" + CARBON_DEFAULT_HOSTNAME + ":" + CARBON_DEFAULT_PORT + "'";
    }
}
