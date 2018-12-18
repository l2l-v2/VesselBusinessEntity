package com.l2l.enterprise.iot.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommonRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int defautDelayHour = 6; //默认港口停留时间
    private int zoomInVal = 1000; // 如果按1000的压缩比，停留一小时只需要3.6s

    public CommonRepository(){
        logger.debug("--"+defautDelayHour+"--"+zoomInVal);
    }

    public Logger getLogger() {
        return logger;
    }

    public int getDefautDelayHour() {
        return defautDelayHour;
    }

    public void setDefautDelayHour(int defautDelayHour) {
        this.defautDelayHour = defautDelayHour;
    }

    public int getZoomInVal() {
        return zoomInVal;
    }

    public void setZoomInVal(int zoomInVal) {
        this.zoomInVal = zoomInVal;
    }
}
