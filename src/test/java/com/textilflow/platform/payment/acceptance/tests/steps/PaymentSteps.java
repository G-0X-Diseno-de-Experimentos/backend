package com.textilflow.platform.payment.acceptance.tests.steps;

import com.textilflow.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.textilflow.platform.payment.domain.model.valueobjects.PaymentAmount;
import com.textilflow.platform.payment.domain.services.PaymentService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Component
public class PaymentSteps {

    @Autowired
    private PaymentService paymentService;

    private CreatePaymentIntentCommand command;
    private String clientSecret;
    private Exception exception;

    @Given("a user with id {long} wants to create a payment intent")
    public void aUserWithIdWantsToCreateAPaymentIntent(Long userId) {
        // Setup user context
    }

    @Given("the payment amount is {double} for subscription plan {string}")
    public void thePaymentAmountIsForSubscriptionPlan(Double amount, String subscriptionPlan) {
        command = new CreatePaymentIntentCommand(
                1L,
                subscriptionPlan,
                new PaymentAmount(BigDecimal.valueOf(amount))
        );
    }

    @When("the user creates the payment intent")
    public void theUserCreatesThePaymentIntent() {
        try {

            clientSecret = paymentService.createPaymentIntent(command);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the payment intent should be created successfully")
    public void thePaymentIntentShouldBeCreatedSuccessfully() {
        assertNotNull(clientSecret);
        assertTrue(clientSecret.startsWith("pi_"));
        assertNull(exception);
    }

    @Then("the client secret should be returned")
    public void theClientSecretShouldBeReturned() {
        assertNotNull(clientSecret);
        assertFalse(clientSecret.isEmpty());
    }

    @Then("an error should be thrown")
    public void anErrorShouldBeThrown() {
        assertNotNull(exception);
    }
}