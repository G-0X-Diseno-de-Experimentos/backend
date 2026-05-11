package com.textilflow.platform.iam.integration;

import com.intuit.karate.junit5.Karate;

public class IamRunner {

    @Karate.Test
    Karate testIam() {
        return Karate.run("Iam").relativeTo(getClass());
    }
}