package com.l2l.enterprise.iot.domain.Annotation;



public class MsgAnnotation extends Annotation {
    private String topic;
    private String scenario;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }
}
