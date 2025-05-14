package com.dindeMarket.config;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of("eu-north-1")) // Замените на ваш регион
                .build();
    }
}
