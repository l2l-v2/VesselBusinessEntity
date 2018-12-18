package com.l2l.enterprise.iot.config;

import com.amazonaws.services.iot.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.enterprise.iot.service.AWSClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Configuration
@SuppressWarnings("all")
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private AWSIotQos topicQos = AWSIotQos.QOS0;

    @Autowired
    private Environment environment;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public AWSClientService awsClientService() throws AWSIotException, InterruptedException, IOException {
      String keysCsv = environment.getRequiredProperty("awsiot.keys");
      AWSClientService awsClientService = new AWSClientService(keysCsv);
      return awsClientService;
    }

}
