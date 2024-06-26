package com.myProject.ecommerce.service;

import com.myProject.ecommerce.dao.CustomerRepository;
import com.myProject.ecommerce.dto.PaymentInfo;
import com.myProject.ecommerce.dto.Purchase;
import com.myProject.ecommerce.dto.PurchaseResponse;
import com.myProject.ecommerce.entity.Customer;
import com.myProject.ecommerce.entity.Order;
import com.myProject.ecommerce.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CheckoutServiceImpl  implements CheckoutService {

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository,
                                @Value("${stripe.key.secret}") String secretKey) {
        this.customerRepository = customerRepository;

        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        //retrieve order info from dto
        Order order = purchase.getOrder();

        //generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        //populate order with order items
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        //populate order with addresses
        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        //populate customer with order
        Customer customer = purchase.getCustomer();

        //check whether a n existing customer
        String theEmail = customer.getEmail();

        Customer customerFromDB = customerRepository.findByEmail(theEmail);

        if (customerFromDB != null) {
            customer = customerFromDB;
        }

        customer.add(order);

        //save to db
        customerRepository.save(customer);


        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "My shop purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());
        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {

        //generate random UUID
        return UUID.randomUUID().toString();

    }
}
