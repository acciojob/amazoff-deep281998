package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    static HashMap<String,Order> orderHashMap= new HashMap<>();
    static HashMap<String,DeliveryPartner> deliveryPartnerHashMap=new HashMap<>();
    static HashMap<String,List<String>> partnerorderdb = new HashMap<>();
    static HashMap<String,String> orderpartnerdb = new HashMap<>();
    public static void addOrder(Order order) {
        orderHashMap.put(order.getId(),order);
    }

    public static void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        deliveryPartnerHashMap.put(partnerId,deliveryPartner);
    }

    public static void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderHashMap.containsKey(orderId) == true && deliveryPartnerHashMap.containsKey(partnerId) ==true){
            orderpartnerdb.put(orderId,partnerId);
            List<String> orderlist = new ArrayList<>();
            if(partnerorderdb.containsKey(partnerId)){
                orderlist = partnerorderdb.get(partnerId);
            }
            orderlist.add(orderId);
            partnerorderdb.put(partnerId,orderlist);
            DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(orderlist.size());
        }

    }

    public static Order getOrderById(String orderId) {
        return orderHashMap.get(orderId);
    }

    public static DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerHashMap.getOrDefault(partnerId , null);
    }

    public static List<String> getOrdersByPartnerId(String partnerId) {
        return partnerorderdb.get(partnerId);
    }

    public static List<String> getAllOrders() {
        List<String> order = new ArrayList<>();
        for(String order1 : orderHashMap.keySet()){
            order.add(order1);
        }
        return order;
    }

    public static Integer getCountOfUnassignedOrders() {
        return orderHashMap.size() - orderpartnerdb.size();

    }

    public static Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
        int leftorder = 0;
        List<String> orderlist = partnerorderdb.get(partnerId);
        for(String order : orderlist){
            int deliverytime = orderHashMap.get(order).getDeliveryTime();
            if(deliverytime > time){
                leftorder++;
            }
        }
        return leftorder;
    }

    public static int getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orderlist = partnerorderdb.get(partnerId);
        int last = 0;
        for(String order : orderlist){
            int temp = orderHashMap.get(order).getDeliveryTime();
            last = Math.max(temp,last);
        }
        return last;
    }

    public static void deletePartnerById(String partnerId) {
        deliveryPartnerHashMap.remove(partnerId);
        List<String> listoforder = partnerorderdb.get(partnerId);
        for(String order : listoforder){
            orderpartnerdb.remove(order);
        }
    }

    public static void deleteOrderById(String orderId) {
        orderHashMap.remove(orderId);
        String partnerid = orderpartnerdb.get(orderId);
        orderpartnerdb.remove(orderId);
        partnerorderdb.get(partnerid).remove(orderId);
        deliveryPartnerHashMap.get(partnerid).setNumberOfOrders(partnerorderdb.get(partnerid).size());
    }

    public static Integer getOrderCountByPartnerId(String partnerId) {
        return partnerorderdb.get(partnerId).size();
    }


}