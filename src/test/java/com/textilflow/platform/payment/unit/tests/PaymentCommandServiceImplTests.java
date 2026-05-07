package com.textilflow.platform.payment.unit.tests;

import com.textilflow.platform.payment.application.internal.commandservices.PaymentCommandServiceImpl;
import com.textilflow.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.textilflow.platform.payment.domain.model.valueobjects.PaymentAmount;
import com.textilflow.platform.payment.infrastructure.stripe.StripePaymentService;
import com.textilflow.platform.payment.application.internal.outboundservices.acl.ExternalConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCommandServiceImplTests {

    @Mock
    private StripePaymentService stripePaymentService;

    @Mock
    private ExternalConfigurationService externalConfigurationService;

    @InjectMocks
    private PaymentCommandServiceImpl paymentCommandService;

    @Test
    void testCreatePaymentIntent_Success() {
        // Given - Usa "basic" o "corporate" en lugar de "premium"
        CreatePaymentIntentCommand command = new CreatePaymentIntentCommand(
                1L,
                "corporate", // Cambiado de "premium" a "corporate"
                new PaymentAmount(new BigDecimal("100.00"))
        );
        String expectedClientSecret = "pi_test_client_secret";

        when(externalConfigurationService.userExists(1L))
                .thenReturn(true);
        when(stripePaymentService.createPaymentIntent(
                eq(10000L), // 100.00 * 100 cents
                eq(1L),
                eq("corporate"))) // Cambiado para coincidir
                .thenReturn(expectedClientSecret);

        // When
        String result = paymentCommandService.createPaymentIntent(command);

        // Then
        assertEquals(expectedClientSecret, result);
        verify(externalConfigurationService).userExists(1L);
        verify(stripePaymentService).createPaymentIntent(10000L, 1L, "corporate");
    }

    @Test
    void testCreatePaymentIntent_UserNotFound() {
        // Given - Usa "basic" en lugar de "premium"
        CreatePaymentIntentCommand command = new CreatePaymentIntentCommand(
                1L,
                "basic", // Cambiado de "premium" a "basic"
                new PaymentAmount(new BigDecimal("100.00"))
        );

        when(externalConfigurationService.userExists(1L))
                .thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            paymentCommandService.createPaymentIntent(command);
        });

        verify(externalConfigurationService).userExists(1L);
        verifyNoInteractions(stripePaymentService);
    }

    @Test
    void testHandlePaymentSuccess_Success() {
        // Given
        String paymentIntentId = "pi_test_123";
        Long userId = 1L;
        String subscriptionPlan = "premium";

        doNothing().when(externalConfigurationService)
                .updateSubscriptionPlan(userId, subscriptionPlan);

        // When
        paymentCommandService.handlePaymentSuccess(paymentIntentId, userId, subscriptionPlan);

        // Then
        verify(externalConfigurationService).updateSubscriptionPlan(userId, subscriptionPlan);
    }

    @Test
    void testHandlePaymentSuccess_Failure() {
        // Given
        String paymentIntentId = "pi_test_123";
        Long userId = 1L;
        String subscriptionPlan = "premium";

        doThrow(new RuntimeException("Update failed"))
                .when(externalConfigurationService)
                .updateSubscriptionPlan(userId, subscriptionPlan);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            paymentCommandService.handlePaymentSuccess(paymentIntentId, userId, subscriptionPlan);
        });

        verify(externalConfigurationService).updateSubscriptionPlan(userId, subscriptionPlan);
    }
}