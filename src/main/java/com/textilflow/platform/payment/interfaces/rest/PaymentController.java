
package com.textilflow.platform.payment.interfaces.rest;

import com.textilflow.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.textilflow.platform.payment.domain.model.valueobjects.PaymentAmount;
import com.textilflow.platform.payment.domain.services.PaymentService;
import com.textilflow.platform.payment.infrastructure.stripe.StripePaymentService;
import com.textilflow.platform.payment.interfaces.rest.resources.CreatePaymentIntentResource;
import com.textilflow.platform.payment.interfaces.rest.resources.PaymentIntentResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Payment REST controller
 * Handles payment operations for subscription upgrades via Stripe integration
 */
@RestController
@RequestMapping(value = "/api/v1/payments", produces = "application/json")
@Tag(name = "Payments", description = "Payment Management Endpoints for Subscription Upgrades")
public class PaymentController {

    private final PaymentService paymentService;
    private final StripePaymentService stripePaymentService;

    @Value("${stripe.webhook.secret:temp_default}")
    private String webhookSecret;

    public PaymentController(PaymentService paymentService, StripePaymentService stripePaymentService) {
        this.paymentService = paymentService;
        this.stripePaymentService = stripePaymentService;
    }

    /**
     * Create payment intent for subscription upgrade
     * POST /api/v1/payments/create-intent
     */
    @PostMapping("/create-intent")
    @Operation(summary = "Create payment intent for subscription upgrade",
            description = "Creates a Stripe payment intent for upgrading user subscription plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment intent created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<PaymentIntentResource> createPaymentIntent(@RequestBody CreatePaymentIntentResource resource) {
        System.out.println("=== PAYMENT CONTROLLER: Creating payment intent ===");
        System.out.println("Request: " + resource);

        try {
            // Validate subscription plan and get amount
            PaymentAmount amount = getAmountForSubscriptionPlan(resource.subscriptionPlan());

            // Create command
            var command = new CreatePaymentIntentCommand(
                    resource.userId(),
                    resource.subscriptionPlan(),
                    amount
            );

            // Create payment intent
            String clientSecret = paymentService.createPaymentIntent(command);

            // Build response
            var response = new PaymentIntentResource(
                    clientSecret,
                    amount.amount(),
                    "usd",
                    resource.subscriptionPlan()
            );

            System.out.println("Payment intent created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid request: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error creating payment intent: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Handle Stripe webhooks for payment confirmation
     * POST /api/v1/payments/webhooks/stripe
     */
    @PostMapping("/webhooks/stripe")
    @Operation(summary = "Handle Stripe webhook events",
            description = "Processes Stripe webhook events for payment confirmations and subscription updates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid webhook signature or payload")
    })
    public ResponseEntity<Map<String, String>> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        System.out.println("=== WEBHOOK: Received Stripe webhook ===");
        System.out.println("Signature header present: " + (sigHeader != null));

        try {
            // Verificación simplificada para TEST/DEMO
            boolean isValid = true;
            if (!webhookSecret.equals("temporal_webhook_secret")) {
                // Solo verificar en producción real
                isValid = stripePaymentService.verifyWebhookSignature(payload, sigHeader, webhookSecret);
            }

            if (!isValid) {
                System.err.println("Invalid webhook signature");
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid signature"));
            }

            // Parse webhook event (simplified - in production you'd parse the full event)
            // For demo purposes, we'll extract payment_intent.succeeded events
            if (payload.contains("payment_intent.succeeded")) {
                handlePaymentSucceeded(payload);
            }

            return ResponseEntity.ok(Map.of("received", "true"));

        } catch (Exception e) {
            System.err.println("Error processing webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Webhook processing failed"));
        }
    }

    /**
     * Helper method to get payment amount based on subscription plan
     */
    private PaymentAmount getAmountForSubscriptionPlan(String subscriptionPlan) {
        return switch (subscriptionPlan.toLowerCase()) {
            case "basic" -> PaymentAmount.forBasicPlan();
            case "corporate" -> PaymentAmount.forCorporatePlan();
            default -> throw new IllegalArgumentException("Invalid subscription plan: " + subscriptionPlan);
        };
    }

    /**
     * Handle payment succeeded webhook event
     */
    private void handlePaymentSucceeded(String payload) {
        try {
            // Extract payment intent ID from webhook payload
            // In production, use proper JSON parsing
            String paymentIntentId = extractPaymentIntentId(payload);

            if (paymentIntentId != null) {
                // Get metadata from payment intent
                Map<String, String> metadata = stripePaymentService.getPaymentIntentMetadata(paymentIntentId);

                String userId = metadata.get("user_id");
                String subscriptionPlan = metadata.get("subscription_plan");

                if (userId != null && subscriptionPlan != null) {
                    // Update user subscription
                    paymentService.handlePaymentSuccess(paymentIntentId, Long.parseLong(userId), subscriptionPlan);
                    System.out.println("✅ Payment processed and subscription updated successfully");
                } else {
                    System.err.println("Missing metadata in payment intent");
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling payment succeeded: " + e.getMessage());
        }
    }

    /**
     * Extract payment intent ID from webhook payload
     * Simple implementation - in production use proper JSON parsing
     */
    private String extractPaymentIntentId(String payload) {
        try {
            // Simple regex to extract payment intent ID
            // In production, use Jackson or similar JSON library
            String pattern = "\"id\":\"(pi_[^\"]+)\"";
            java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = r.matcher(payload);

            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            System.err.println("Error extracting payment intent ID: " + e.getMessage());
        }
        return null;
    }
}