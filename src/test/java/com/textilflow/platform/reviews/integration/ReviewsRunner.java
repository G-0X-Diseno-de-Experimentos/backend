package com.textilflow.platform.reviews.integration;

import com.intuit.karate.junit5.Karate;

public class ReviewsRunner {
    @Karate.Test
    Karate testReviews() {
        return Karate.run("reviews").relativeTo(getClass());
    }
}