package com.textilflow.platform.payment.domain.services;

import com.textilflow.platform.payment.domain.model.commands.CreatePaymentIntentCommand;

/**
 * Payment domain service interface
 */
public interface PaymentService {

    /**
     * Create payment intent for subscription
     */
    String createPaymentIntent(CreatePaymentIntentCommand command);

    /**
     * Handle successful payment completion
     */
    void handlePaymentSuccess(String paymentIntentId, Long userId, String subscriptionPlan);
}