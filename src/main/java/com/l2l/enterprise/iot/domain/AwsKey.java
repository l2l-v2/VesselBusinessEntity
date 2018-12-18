package com.l2l.enterprise.iot.domain;

public class AwsKey {
    private String vid;
    private String thingName;
    private String localPath;
    private String clientEndpoint;
    private String defaultTopic;
    private String customTopic;

    public AwsKey(String vid, String thingName, String localPath, String clientEndpoint, String defaultTopic, String customTopic) {
        this.vid = vid;
        this.thingName = thingName;
        this.localPath = localPath;
        this.clientEndpoint = clientEndpoint;
        this.defaultTopic = defaultTopic;
        this.customTopic = customTopic;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getClientEndpoint() {
        return clientEndpoint;
    }

    public void setClientEndpoint(String clientEndpoint) {
        this.clientEndpoint = clientEndpoint;
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
