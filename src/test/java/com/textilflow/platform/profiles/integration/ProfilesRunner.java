package com.textilflow.platform.profiles.integration;

import com.intuit.karate.junit5.Karate;

public class ProfilesRunner {
    @Karate.Test
    Karate testProfiles() {
        return Karate.run("profiles").relativeTo(getClass());
    }
}