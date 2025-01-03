package com.gaurav.linkedin.user_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfiguration {
    @Bean
    public NewTopic userCreatedTopic(){
        return new NewTopic("user-created-topic",3,(short) 1);
    }
}
