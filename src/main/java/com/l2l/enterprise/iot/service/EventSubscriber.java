package com.l2l.enterprise.iot.service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This class extends {@link AWSIotTopic} to receive messages from a subscribed
 * topic.
 * Author : bqzhu
 */
@SuppressWarnings("all")
public class EventSubscriber extends AWSIotTopic {
    private static final Logger logger = LoggerFactory.getLogger(EventSubscriber.class);

    private EventHandler eventHandler;


    public EventSubscriber(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        //called when a message is received.
        String receivedTopic = message.getTopic();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootJNode = null;
        try {
            rootJNode = objectMapper.readTree(message.getStringPayload());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String msgType = rootJNode.findValue("msgType").asText();
        String to = rootJNode.findValue("To").asText();
        logger.debug("message :" + msgType + " from : " + to);
        try {
            if(receivedTopic.equals("IoT/V"+to+"/track")){
                eventHandler.track(to  , rootJNode);
            }
            if(receivedTopic.equals("IoT/V"+to+"/voyaging")){

            }
            if(receivedTopic.equals("IoT/V"+to+"/delay")){

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
}
