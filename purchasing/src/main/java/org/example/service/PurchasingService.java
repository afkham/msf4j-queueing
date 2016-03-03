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

import java.util.UUID;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */
@Path("/purchasing")
public class PurchasingService {
    private static final int REORDER_LEVEL = 100;
    private static final int REORDER_QUANTITY = 50;

    public PurchasingService() {
        InventoryManager.add("i001", 200);
        InventoryManager.add("i002", 150);
    }

    @POST
    @Path("/")
    public Response post(Order order) {
        String itemCode = order.getItemCode();
        if (!InventoryManager.itemExists(itemCode)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UUID orderId = UUID.randomUUID();
        if (InventoryManager.getQuantity(itemCode) <= REORDER_LEVEL) {
            order.setOrderId(orderId.toString());
            reorder(order);
        }
        if (InventoryManager.getQuantity(itemCode) < order.getQuantity()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Insufficient stock").build();
        }
        InventoryManager.remove(itemCode, order.getQuantity());
        UUID orderId2 = UUID.randomUUID();
        if (InventoryManager.getQuantity(itemCode) <= REORDER_LEVEL) {
            order.setOrderId(orderId2.toString());
            reorder(order);
        }

        return Response.status(Response.Status.OK).entity(orderId).build();
    }

    private void reorder(Order order) {
        System.out.println("Reordering item: " + order.getItemCode());
        order.setQuantity(REORDER_QUANTITY);
        try {
            ReorderRequestMessageSender.sendMessage(order);
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }
}
