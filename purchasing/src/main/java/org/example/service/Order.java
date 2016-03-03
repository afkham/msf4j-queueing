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

import java.io.Serializable;

/**
 * Order.
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 6867749351899757703L;

    private String itemCode;
    private String name;
    private int quantity;
    private String orderId;

    public Order() {
    }

    public Order(String itemCode) {
        this.itemCode = itemCode;
    }

    public Order(String itemCode, String name, int quantity) {
        this.itemCode = itemCode;
        this.name = name;
        this.quantity = quantity;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "itemCode='" + itemCode + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}