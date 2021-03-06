package com.l2l.enterprise.iot.domain;

import com.l2l.enterprise.iot.domain.Annotation.Annotation;
import com.l2l.enterprise.iot.domain.Annotation.MsgAnnotation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DelayMsg implements Msg,Serializable {
    private List<String> pids;//delete
    private String connectorType;
    private String delayx;
    private String delayy;
    private String topic;
    private Map<String,List<Destination>> destinationMap;

    public DelayMsg() {
    }

    public DelayMsg(String connectorType) {
        this.connectorType = connectorType;
    }

    public DelayMsg(List<String> pids, String connectorType, String delayx, String delayy, String topic) {
        this.pids = pids;
        this.connectorType = connectorType;
        this.delayx = delayx;
        this.delayy = delayy;
        this.topic = topic;
    }

    public DelayMsg(List<String> pids, String connectorType, String delayx, String delayy, String topic, Map<String, List<Destination>> destinationMap) {
        this.pids = pids;
        this.connectorType = connectorType;
        this.delayx = delayx;
        this.delayy = delayy;
        this.topic = topic;
        this.destinationMap = destinationMap;
    }

    public List<String> getPids() {
        return pids;
    }

    public void setPids(List<String> pids) {
        this.pids = pids;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public Map<String, List<Destination>> getDestinationMap() {
        return destinationMap;
    }

    public void setDestinationMap(Map<String, List<Destination>> destinationMap) {
        this.destinationMap = destinationMap;
    }

    public String getDelayx() {
        return delayx;
    }

    public void setDelayx(String delayx) {
        this.delayx = delayx;
    }

    public String getDelayy() {
        return delayy;
    }

    public void setDelayy(String delayy) {
        this.delayy = delayy;
    }
}
