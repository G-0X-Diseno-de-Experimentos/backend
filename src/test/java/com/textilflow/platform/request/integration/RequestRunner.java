package com.textilflow.platform.request.integration;

import com.intuit.karate.junit5.Karate;

public class RequestRunner {
    @Karate.Test
    Karate testRequests() {
        return Karate.run("requests").relativeTo(getClass());
    }
}