package com.l2l.enterprise.iot.domain;

import com.amazonaws.services.iot.client.AWSIotMqttClient;

public class IoTClient {
    private String vid;
    private VesselDevice vesselDevice;
    private AWSIotMqttClient awsIotMqttClient;
    private String defaultTopic;
    private String customTopic;

    public String getUpdateStatusTopic(){
        return customTopic+"status";
    }

    public String getUpdateAWSShadowTopic(){
        return defaultTopic+"update";
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public VesselDevice getVesselDevice() {
        return vesselDevice;
    }

    public void setVesselDevice(VesselDevice vesselDevice) {
        this.vesselDevice = vesselDevice;
    }

    public AWSIotMqttClient getAwsIotMqttClient() {
        return awsIotMqttClient;
    }

    public void setAwsIotMqttClient(AWSIotMqttClient awsIotMqttClient) {
        this.awsIotMqttClient = awsIotMqttClient;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public String getCustomTopic() {
        return customTopic;
    }

    public void setCustomTopic(String customTopic) {
        this.customTopic = customTopic;
    }
}
