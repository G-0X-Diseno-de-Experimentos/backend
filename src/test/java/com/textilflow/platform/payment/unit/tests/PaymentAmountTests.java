package com.textilflow.platform.payment.unit.tests;

import com.textilflow.platform.payment.domain.model.valueobjects.PaymentAmount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentAmountTests {

    @Test
    void testValidPaymentAmount() {
        // Given
        BigDecimal validAmount = new BigDecimal("100.00");

        // When
        PaymentAmount paymentAmount = new PaymentAmount(validAmount);

        // Then
        assertEquals(0, validAmount.compareTo(paymentAmount.amount()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1.0", "0.0", "-100.0"})
    void testInvalidPaymentAmount(String invalidAmount) {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new PaymentAmount(new BigDecimal(invalidAmount));
        });
    }

    @Test
    void testPaymentAmountEquality() {
        // Given
        PaymentAmount amount1 = new PaymentAmount(new BigDecimal("100.00"));
        PaymentAmount amount2 = new PaymentAmount(new BigDecimal("100.00"));
        PaymentAmount amount3 = new PaymentAmount(new BigDecimal("200.00"));

        // Then
        assertEquals(amount1, amount2);
        assertNotEquals(amount1, amount3);
    }

    @Test
    void testToStripeCentsConversion() {
        // Given
        PaymentAmount amount = new PaymentAmount(new BigDecimal("49.99"));

        // When
        Long cents = amount.toStripeCents();

        // Then
        assertEquals(4999L, cents);
    }

    @Test
    void testForBasicPlan() {
        // When
        PaymentAmount basicPlanAmount = PaymentAmount.forBasicPlan();

        // Then
        assertEquals(0, new BigDecimal("9.99").compareTo(basicPlanAmount.amount()));
    }

    @Test
    void testForCorporatePlan() {
        // When
        PaymentAmount corporatePlanAmount = PaymentAmount.forCorporatePlan();

        // Then
        assertEquals(0, new BigDecimal("49.99").compareTo(corporatePlanAmount.amount()));
    }
}