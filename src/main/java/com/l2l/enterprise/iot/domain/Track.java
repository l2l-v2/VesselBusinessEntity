package com.l2l.enterprise.iot.domain;

import lombok.Data;

import java.util.List;

@Data
public class Track {
    private String vid;
    private List<String> destinations;
    private List<Step>  steps;
}
