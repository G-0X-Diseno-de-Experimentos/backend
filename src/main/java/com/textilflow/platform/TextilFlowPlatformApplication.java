package com.textilflow.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@EnableJpaAuditing
public class TextilFlowPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextilFlowPlatformApplication.class, args);
    }

}