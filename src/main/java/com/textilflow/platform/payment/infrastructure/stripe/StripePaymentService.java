package com.textilflow.platform.payment.infrastructure.stripe;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Stripe payment service implementation
 * Handles direct integration with Stripe API for payment processing
 */
@Service
public class StripePaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    /**
     * Initialize Stripe configuration after bean creation
     */
    @PostConstruct
    public void init() {
        System.out.println("=== STRIPE INITIALIZATION DEBUG ===");
        System.out.println("Raw stripeSecretKey value: " + stripeSecretKey);
        System.out.println("Stripe secret key present: " + (stripeSecretKey != null && !stripeSecretKey.isEmpty()));
        System.out.println("Stripe secret key length: " + (stripeSecretKey != null ? stripeSecretKey.length() : 0));
        System.out.println("Stripe secret key starts with 'sk_': " + (stripeSecretKey != null && stripeSecretKey.startsWith("sk_")));

        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            System.err.println("❌ STRIPE SECRET KEY IS NULL OR EMPTY!");
            return;
        }

        if (stripeSecretKey.equals("your_stripe_secret_key")) {
            System.err.println("❌ STRIPE SECRET KEY IS STILL THE PLACEHOLDER VALUE!");
            return;
        }

        try {
            Stripe.apiKey = stripeSecretKey;
            System.out.println("✅ Stripe initialized successfully");
            System.out.println("Stripe.apiKey set to: " + (Stripe.apiKey != null ? Stripe.apiKey.substring(0, 10) + "..." : "null"));
        } catch (Exception e) {
            System.err.println("❌ Error setting Stripe API key: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create payment intent for subscription upgrade
     * Returns client secret for frontend payment completion
     */
    public String createPaymentIntent(Long amountInCents, Long userId, String subscriptionPlan) {
        System.out.println("=== STRIPE PAYMENT INTENT CREATION DEBUG ===");
        System.out.println("Amount in cents: " + amountInCents);
        System.out.println("User ID: " + userId);
        System.out.println("Subscription plan: " + subscriptionPlan);
        System.out.println("Current Stripe.apiKey: " + (Stripe.apiKey != null ? Stripe.apiKey.substring(0, 10) + "..." : "null"));

        try {
            System.out.println("🔄 Creating metadata...");
            // Create metadata for tracking
            Map<String, String> metadata = new HashMap<>();
            metadata.put("user_id", userId.toString());
            metadata.put("subscription_plan", subscriptionPlan);
            metadata.put("context", "subscription_upgrade");
            System.out.println("✅ Metadata created: " + metadata);

            System.out.println("🔄 Building payment intent parameters...");
            // Build payment intent parameters
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd")
                    .putAllMetadata(metadata)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();
            System.out.println("✅ Payment intent parameters built successfully");

            System.out.println("🔄 Calling Stripe API...");
            // Create payment intent via Stripe API
            PaymentIntent intent = PaymentIntent.create(params);
            System.out.println("✅ Stripe API call successful!");

            System.out.println("Payment Intent created successfully");
            System.out.println("Payment Intent ID: " + intent.getId());
            System.out.println("Client Secret: " + intent.getClientSecret());

            return intent.getClientSecret();

        } catch (StripeException e) {
            System.err.println("❌ STRIPE ERROR OCCURRED:");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("HTTP status: " + e.getStatusCode());
            System.err.println("Request ID: " + e.getRequestId());
            e.printStackTrace();
            throw new RuntimeException("Stripe API error: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("❌ UNEXPECTED ERROR:");
            System.err.println("Error class: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error during payment intent creation: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve payment intent details from Stripe
     * Used for verification and webhook processing
     */
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) {
        try {
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
            System.out.println("Retrieved Payment Intent: " + paymentIntentId);
            System.out.println("Status: " + intent.getStatus());
            return intent;
        } catch (StripeException e) {
            System.err.println("Error retrieving payment intent: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve payment intent: " + e.getMessage(), e);
        }
    }

    /**
     * Verify webhook signature for security
     * Ensures webhook requests actually come from Stripe
     */
    public boolean verifyWebhookSignature(String payload, String sigHeader, String webhookSecret) {
        try {
            com.stripe.model.Event event = com.stripe.net.Webhook.constructEvent(
                    payload, sigHeader, webhookSecret
            );
            System.out.println("Webhook signature verified successfully");
            System.out.println("Event type: " + event.getType());
            return true;
        } catch (Exception e) {
            System.err.println("Webhook signature verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get payment intent metadata for processing
     * Extracts user and subscription information from Stripe metadata
     */
    public Map<String, String> getPaymentIntentMetadata(String paymentIntentId) {
        try {
            PaymentIntent intent = retrievePaymentIntent(paymentIntentId);
            Map<String, String> metadata = intent.getMetadata();

            System.out.println("Payment Intent metadata retrieved:");
            metadata.forEach((key, value) ->
                    System.out.println("  " + key + ": " + value)
            );

            return metadata;
        } catch (Exception e) {
            System.err.println("Error getting payment intent metadata: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Helper method to validate payment intent status
     * Returns true if payment was successful
     */
    public boolean isPaymentSuccessful(String paymentIntentId) {
        try {
            PaymentIntent intent = retrievePaymentIntent(paymentIntentId);
            boolean isSuccessful = "succeeded".equals(intent.getStatus());

            System.out.println("Payment status check for " + paymentIntentId + ": " +
                    (isSuccessful ? "SUCCESS" : "NOT_SUCCESS (" + intent.getStatus() + ")"));

            return isSuccessful;
        } catch (Exception e) {
            System.err.println("Error checking payment status: " + e.getMessage());
            return false;
        }
    }
}