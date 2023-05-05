package com.emansy.eventservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

//    @Bean
//    public NewTopic javaTopic_json() {
//        return TopicBuilder.name("events-request")
//                .build();
//    }
}
