package com.textilflow.platform.payment.integration.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.textilflow.platform.payment.domain.services.PaymentService;
import com.textilflow.platform.payment.infrastructure.stripe.StripePaymentService;
import com.textilflow.platform.payment.interfaces.rest.PaymentController;
import com.textilflow.platform.payment.interfaces.rest.resources.CreatePaymentIntentResource;
import com.textilflow.platform.payment.interfaces.rest.resources.PaymentIntentResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTests {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Eliminada anotación @Autowired

    @Mock
    private PaymentService paymentService;

    @Mock
    private StripePaymentService stripePaymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testCreatePaymentIntent_Success() throws Exception {
        // Given
        CreatePaymentIntentResource resource = new CreatePaymentIntentResource(
                1L,
                "corporate"
        );

        when(paymentService.createPaymentIntent(any()))
                .thenReturn("test_client_secret_123");

        // When & Then
        mockMvc.perform(post("/api/v1/payments/create-intent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientSecret").value("test_client_secret_123"))
                .andExpect(jsonPath("$.subscriptionPlan").value("corporate"));
    }

    @Test
    void testCreatePaymentIntent_InvalidSubscription() throws Exception {
        // Given - Usa un plan que NO pase la validación inicial
        CreatePaymentIntentResource resource = new CreatePaymentIntentResource(
                1L,
                "invalid-plan"  // Plan que fallará en getAmountForSubscriptionPlan()
        );

        // NO configures el mock del servicio porque la validación fallará antes

        // When & Then
        mockMvc.perform(post("/api/v1/payments/create-intent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePaymentIntent_InvalidUserId() throws Exception {
        // Given - Usa valores que pasen la validación inicial
        CreatePaymentIntentResource resource = new CreatePaymentIntentResource(
                1L,  // ID válido para el constructor
                "corporate"
        );

        // Mock service behavior para simular error de usuario no encontrado
        when(paymentService.createPaymentIntent(any()))
                .thenThrow(new IllegalArgumentException("User not found"));

        // When & Then
        mockMvc.perform(post("/api/v1/payments/create-intent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isBadRequest());
    }
}