package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public static void addOrder(Order order) {
        OrderRepository.addOrder(order);
    }

    public static void addPartner(String partnerId) {
        OrderRepository.addPartner(partnerId);
    }

    public static void addOrderPartnerPair(String orderId, String partnerId) {
        OrderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    public static Order getOrderById(String orderId) {

        return OrderRepository.getOrderById(orderId);
    }

    public static DeliveryPartner getPartnerById(String partnerId) {

        return OrderRepository.getPartnerById(partnerId);
    }

    public static List<String> getOrdersByPartnerId(String partnerId) {
        return OrderRepository.getOrdersByPartnerId(partnerId);
    }

    public static List<String> getAllOrders() {

        return OrderRepository.getAllOrders();
    }

    public static Integer getCountOfUnassignedOrders() {

        return OrderRepository.getCountOfUnassignedOrders();
    }

    public static Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String str[] = time.split(":");
        int timeg = (Integer.parseInt(str[0])*60) + Integer.parseInt(str[1]);
        return OrderRepository.getOrdersLeftAfterGivenTimeByPartnerId(timeg, partnerId);
    }

    public static String getLastDeliveryTimeByPartnerId(String partnerId) {
        int time = OrderRepository.getLastDeliveryTimeByPartnerId(partnerId);
        String HH = String.valueOf(time/60);
        String MM = String.valueOf(time%60);
        if(HH.length() < 2){
            HH = '0' +HH;
        }
        if(MM.length() < 2){
            MM = '0'+MM;
        }
        return HH+':'+MM;
    }

    public static void deletePartnerById(String partnerId) {

        OrderRepository.deletePartnerById(partnerId);
    }

    public static void deleteOrderById(String orderId) {
        OrderRepository.deleteOrderById(orderId);
    }

    public static Integer getOrderCountByPartnerId(String partnerId) {
        return OrderRepository.getOrderCountByPartnerId(partnerId);
    }
}