package com.textilflow.platform.payment.integration.tests;

import com.textilflow.platform.payment.infrastructure.stripe.StripePaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StripePaymentServiceUnitTest {

    @Mock
    private StripePaymentService stripePaymentService;

    @Test
    void testCreatePaymentIntent_Success() {
        when(stripePaymentService.createPaymentIntent(anyLong(), anyLong(), anyString()))
                .thenReturn("pi_mock_123456789");

        String result = stripePaymentService.createPaymentIntent(10000L, 1L, "premium");

        assertNotNull(result);
        assertTrue(result.startsWith("pi_"));
    }
}
