package com.l2l.enterprise.iot.domain;

import java.util.List;

public class Step {
    private List<VesselState> vesselStates;
    private String prePort;
    private String nextPort;


    public List<VesselState> getVesselStates() {
        return vesselStates;
    }

    public void setVesselStates(List<VesselState> vesselStates) {
        this.vesselStates = vesselStates;
    }

    public String getPrePort() {
        return prePort;
    }

    public void setPrePort(String prePort) {
        this.prePort = prePort;
    }

    public String getNextPort() {
        return nextPort;
    }

    public void setNextPort(String nextPort) {
        this.nextPort = nextPort;
    }
}
