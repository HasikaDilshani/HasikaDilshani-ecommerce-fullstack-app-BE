package com.myProject.ecommerce.service;

import com.myProject.ecommerce.dto.PaymentInfo;
import com.myProject.ecommerce.dto.Purchase;
import com.myProject.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Service;

@Service
public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
