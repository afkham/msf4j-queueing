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

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: class level comment
 */
public class InventoryManager {
    private static Map<String, Integer> inventory = new HashMap<>();

    public static void add(String itemCode, int quantity) {
        if (inventory.get(itemCode) == null) {
            inventory.put(itemCode, quantity);
        } else {
            inventory.put(itemCode, inventory.get(itemCode) + quantity);
        }
    }

    public static void remove(String itemCode, int quantity) {
        if (inventory.get(itemCode) != null) {
            inventory.put(itemCode, inventory.get(itemCode) - quantity);
        }
    }

    public static boolean itemExists(String itemCode) {
        return inventory.containsKey(itemCode);
    }

    public static int getQuantity(String itemCode) {
        return inventory.get(itemCode);
    }
}
