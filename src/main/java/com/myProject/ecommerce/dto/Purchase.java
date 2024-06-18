package com.myProject.ecommerce.dto;

import com.myProject.ecommerce.entity.Address;
import com.myProject.ecommerce.entity.Customer;
import com.myProject.ecommerce.entity.Order;
import com.myProject.ecommerce.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
