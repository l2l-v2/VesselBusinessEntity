package com.l2l.enterprise.iot.domain;

import lombok.Data;

import java.util.List;

@Data
public class Step {
    private List<VesselState> vesselStates;
    private String prePort;
    private String nextPort;
}
