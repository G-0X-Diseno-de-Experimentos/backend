package com.textilflow.platform.iam.integration;

import com.intuit.karate.junit5.Karate;

public class AuthenticationRunner {
    @Karate.Test
    Karate testAuthentication() {
        return Karate.run("authentication").relativeTo(getClass());
    }
}