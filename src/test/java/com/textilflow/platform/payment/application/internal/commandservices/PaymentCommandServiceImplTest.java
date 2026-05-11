package com.textilflow.platform.payment.application.internal.commandservices;


import com.textilflow.platform.payment.application.internal.outboundservices.acl.ExternalConfigurationService;
import com.textilflow.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.textilflow.platform.payment.domain.model.valueobjects.PaymentAmount;
import com.textilflow.platform.payment.infrastructure.stripe.StripePaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCommandServiceImplTest {

    @Mock
    private StripePaymentService stripePaymentService;

    @Mock
    private ExternalConfigurationService externalConfigurationService;

    @InjectMocks
    private PaymentCommandServiceImpl paymentCommandService;

    @Test
    @DisplayName("createPaymentIntent debe crear payment intent exitosamente cuando el usuario existe")
    void testCreatePaymentIntent_Success() {

        // ARRANGE
        CreatePaymentIntentCommand command = new CreatePaymentIntentCommand(
                1L,
                "corporate",
                new PaymentAmount(new BigDecimal("100.00"))
        );

        String expectedClientSecret = "pi_test_client_secret";

        when(externalConfigurationService.userExists(1L)).thenReturn(true);
        when(stripePaymentService.createPaymentIntent(10000L, 1L, "corporate"))
                .thenReturn(expectedClientSecret);

        // ACT
        String result = paymentCommandService.createPaymentIntent(command);

        // ASSERT
        assertEquals(expectedClientSecret, result);
        verify(externalConfigurationService).userExists(1L);
        verify(stripePaymentService).createPaymentIntent(10000L, 1L, "corporate");
    }

    @Test
    @DisplayName("createPaymentIntent debe lanzar excepción cuando el usuario no existe")
    void testCreatePaymentIntent_UserNotFound() {

        // ARRANGE
        CreatePaymentIntentCommand command = new CreatePaymentIntentCommand(
                1L,
                "basic",
                new PaymentAmount(new BigDecimal("100.00"))
        );

        when(externalConfigurationService.userExists(1L)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> paymentCommandService.createPaymentIntent(command));

        verify(externalConfigurationService).userExists(1L);
        verifyNoInteractions(stripePaymentService);
    }

    @Test
    @DisplayName("handlePaymentSuccess debe actualizar suscripción correctamente")
    void testHandlePaymentSuccess_Success() {

        // ARRANGE
        String paymentIntentId = "pi_test_123";
        Long userId = 1L;
        String subscriptionPlan = "premium";

        doNothing().when(externalConfigurationService)
                .updateSubscriptionPlan(userId, subscriptionPlan);

        // ACT
        paymentCommandService.handlePaymentSuccess(paymentIntentId, userId, subscriptionPlan);

        // ASSERT
        verify(externalConfigurationService)
                .updateSubscriptionPlan(userId, subscriptionPlan);
    }

    @Test
    @DisplayName("handlePaymentSuccess debe lanzar excepción si falla actualización")
    void testHandlePaymentSuccess_Failure() {

        // ARRANGE
        String paymentIntentId = "pi_test_123";
        Long userId = 1L;
        String subscriptionPlan = "premium";

        doThrow(new RuntimeException("Update failed"))
                .when(externalConfigurationService)
                .updateSubscriptionPlan(userId, subscriptionPlan);

        // ACT + ASSERT
        assertThrows(RuntimeException.class,
                () -> paymentCommandService.handlePaymentSuccess(paymentIntentId, userId, subscriptionPlan));

        verify(externalConfigurationService)
                .updateSubscriptionPlan(userId, subscriptionPlan);
    }

    @Test
    @DisplayName("CreatePaymentIntentCommand debe fallar si subscriptionPlan es inválido")
    void shouldThrow_WhenSubscriptionPlanInvalid() {

        assertThrows(IllegalArgumentException.class,
                () -> new CreatePaymentIntentCommand(
                        1L,
                        "",
                        new PaymentAmount(new BigDecimal("100.00"))
                ));
    }


    @Test
    @DisplayName("PaymentAmount debe lanzar excepción si el monto es null o inválido")
    void shouldThrow_WhenAmountInvalid() {

        // ARRANGE + ACT
        assertThrows(IllegalArgumentException.class,
                () -> new PaymentAmount(null));

        // ASSERT implícito (excepción esperada)
    }

    @Test
    @DisplayName("createPaymentIntent debe propagar error cuando Stripe falla")
    void createPaymentIntent_ShouldThrow_WhenStripeFails() {

        // ARRANGE
        CreatePaymentIntentCommand command =
                new CreatePaymentIntentCommand(1L, "basic",
                        new PaymentAmount(new BigDecimal("100.00")));

        when(externalConfigurationService.userExists(1L)).thenReturn(true);
        when(stripePaymentService.createPaymentIntent(anyLong(), anyLong(), anyString()))
                .thenThrow(new RuntimeException("Stripe error"));

        // ACT + ASSERT
        assertThrows(RuntimeException.class,
                () -> paymentCommandService.createPaymentIntent(command));
    }

    @Test
    @DisplayName("createPaymentIntent debe convertir correctamente monto a centavos")
    void createPaymentIntent_ShouldConvertAmountCorrectly() {

        // ARRANGE
        CreatePaymentIntentCommand command =
                new CreatePaymentIntentCommand(1L, "basic",
                        new PaymentAmount(new BigDecimal("10.50")));

        when(externalConfigurationService.userExists(1L)).thenReturn(true);
        when(stripePaymentService.createPaymentIntent(anyLong(), anyLong(), anyString()))
                .thenReturn("secret");

        // ACT
        paymentCommandService.createPaymentIntent(command);

        // ASSERT
        verify(stripePaymentService)
                .createPaymentIntent(1050L, 1L, "basic");
    }
}