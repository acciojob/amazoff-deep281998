package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    static HashMap<String,Order> orderHashMap= new HashMap<>();
    static HashMap<String,DeliveryPartner> deliveryPartnerHashMap=new HashMap<>();
    static HashMap<String,List<Order>> listHashMap = new HashMap<>();
    static HashMap<String,String> assignedOrderMap = new HashMap<>();
    public static void addOrder(Order order) {
        String orderid = order.getId();
        orderHashMap.put(orderid,order);
    }

    public static void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        deliveryPartnerHashMap.put(partnerId,deliveryPartner);
    }

    public static void addOrderPartnerPair(String orderId, String partnerId) {
        if(listHashMap.containsKey(partnerId) == true){
            List<Order> order = listHashMap.get(partnerId);
            order.add(orderHashMap.get(orderId));
            listHashMap.put(partnerId,order);
        }
        else {
            List<Order> orders = new ArrayList<>();
            orders.add(orderHashMap.get(orderId));
            listHashMap.put(partnerId,orders);
        }
        if(deliveryPartnerHashMap.containsKey(partnerId) == true){
            DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
            deliveryPartnerHashMap.put(partnerId,deliveryPartner);
        }
        else {
            DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
            deliveryPartner.setNumberOfOrders(1);
            deliveryPartnerHashMap.put(partnerId,deliveryPartner);
        }
        assignedOrderMap.put(orderId,partnerId);
        orderHashMap.remove(orderId);

    }

    public static Order getOrderById(String orderId) {
        if(orderHashMap.containsKey(orderId) == true){
            return orderHashMap.get(orderId);
        }
        if(assignedOrderMap.containsKey(orderId)==true){
            String deliveryid = assignedOrderMap.get(orderId);
            List<Order> orderList = listHashMap.get(deliveryid);
            for(Order order : orderList){
                if(order.getId() == orderId){
                    return order;
                }
            }
        }
        return new Order();
    }

    public static DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerHashMap.getOrDefault(partnerId , null);
    }

    public static List<String> getOrdersByPartnerId(String partnerId) {
        ArrayList<String> orderNames= new ArrayList<>();
        List<Order> orderList=listHashMap.getOrDefault(partnerId,new ArrayList<>());
        for(Order order : orderList){
            orderNames.add(order.getId());
        }
        return orderNames;
    }

    public static List<String> getAllOrders() {
        List<String> orderlist = new ArrayList<>();
        for (String orderid : orderHashMap.keySet()){
            orderlist.add(orderid);
        }
        for(String orderid : assignedOrderMap.keySet()){
            orderlist.add(orderid);
        }
        return orderlist;
    }

    public static Integer getCountOfUnassignedOrders() {
        int ans= orderHashMap.size();
        Integer integer = ans;
        return integer;
    }

    public static Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int ordersLeft=0;
        int targetHours = Integer.parseInt(time.substring(0,2));
        int targetMinutes = Integer.parseInt(time.substring(3));
        int targetTimeInMinutes=targetHours * 60 + targetMinutes;

        List<Order> orderList = listHashMap.getOrDefault(partnerId, new ArrayList<>());
        for (Order order : orderList) {
            int deliveryTimeInMinutes = order.getDeliveryTime();
            if (deliveryTimeInMinutes >= targetTimeInMinutes) {
                ordersLeft++;
            }
        }
        Integer integer=ordersLeft;
        return integer;
    }

    public static String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<Order> orderList = listHashMap.getOrDefault(partnerId,new ArrayList<>());
        int last=Integer.MIN_VALUE;
        if (!orderList.isEmpty()) {
            for(Order order : orderList)
            {
                int time =order.getDeliveryTime();
                last = Math.max(time,last);
            }
            int deliveryTimeInMinutes = last;
            int hours = deliveryTimeInMinutes / 60;
            int minutes = deliveryTimeInMinutes % 60;

            return String.format("%02d:%02d", hours, minutes);
        }

        return null;
    }

    public static void deletePartnerById(String partnerId) {
        if(listHashMap.containsKey(partnerId)==true){
         List<Order> orderList = listHashMap.get(partnerId);
         for(Order order : orderList){
             String orderid = order.getId();
             if(assignedOrderMap.containsKey(orderid) == true) {
                 assignedOrderMap.remove(orderid);
             }
             orderHashMap.put(orderid,order);
         }
         listHashMap.remove(partnerId);
        }
    }

    public static void deleteOrderById(String orderId) {
        orderHashMap.remove(orderId);


        if(assignedOrderMap.containsKey(orderId))
        {
            String partnerId = assignedOrderMap.get(orderId);
            DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
//           int noOfOrders = deliveryPartner.getNumberOfOrders();
//           deliveryPartner.setNumberOfOrders(noOfOrders-1);

            assignedOrderMap.remove(orderId);

            List<Order> orderList = listHashMap.getOrDefault(deliveryPartner.getId(),new ArrayList<>());

            for(Order order : orderList)
            {
                String s = order.getId();
                if(s.equals(orderId))
                {
                    orderList.remove(order);
                    listHashMap.put(deliveryPartner.getId(),orderList);
                    deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()-1);
                    return;
                }
            }
        }

    }

    public static Integer getOrderCountByPartnerId(String partnerId) {
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
        int ans = deliveryPartner.getNumberOfOrders();
        Integer integer = ans;
        return integer;
    }
}