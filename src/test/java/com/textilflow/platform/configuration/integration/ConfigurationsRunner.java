package com.textilflow.platform.configuration.integration;

import com.intuit.karate.junit5.Karate;

public class ConfigurationsRunner {
    @Karate.Test
    Karate testConfigurations() {
        return Karate.run("configurations").relativeTo(getClass());
    }
}