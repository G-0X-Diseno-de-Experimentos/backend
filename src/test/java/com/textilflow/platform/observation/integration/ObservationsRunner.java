package com.textilflow.platform.observation.integration;

import com.intuit.karate.junit5.Karate;

public class ObservationsRunner {
    @Karate.Test
    Karate testObservations() {
        return Karate.run("observations").relativeTo(getClass());
    }
}