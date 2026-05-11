package com.textilflow.platform.iam.integration;

import com.intuit.karate.junit5.Karate;

public class IamRunner {

    @Karate.Test
    Karate testIam() {
        // Debe coincidir EXACTAMENTE con el nombre del archivo físico
        return Karate.run("Iam").relativeTo(getClass());
    }
}