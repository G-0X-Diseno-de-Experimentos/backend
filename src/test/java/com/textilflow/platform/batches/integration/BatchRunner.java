package com.textilflow.platform.batches.integration;

import com.intuit.karate.junit5.Karate;

public class BatchRunner {
    @Karate.Test
    Karate testBatches() {
        return Karate.run("batches").relativeTo(getClass());
    }
}