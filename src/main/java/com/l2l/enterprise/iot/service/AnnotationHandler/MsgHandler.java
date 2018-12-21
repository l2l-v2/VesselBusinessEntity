package com.l2l.enterprise.iot.service.AnnotationHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2l.enterprise.iot.channel.DelayMsgChannel;
import com.l2l.enterprise.iot.domain.DelayMsg;
import com.l2l.enterprise.iot.domain.Destination;
import com.l2l.enterprise.iot.domain.IoTClient;
import com.l2l.enterprise.iot.domain.VesselDevice;
import com.l2l.enterprise.iot.repository.CommonRepository;
import com.l2l.enterprise.iot.repository.LocationRepository;
import com.l2l.enterprise.iot.repository.TrackRepository;
import com.l2l.enterprise.iot.service.AWSClientService;
import com.l2l.enterprise.iot.util.DateUtil;
import com.sun.javafx.collections.MappingChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@EnableBinding(DelayMsgChannel.class)
public class MsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(MsgHandler.class);

    @Autowired
    BinderAwareChannelResolver binderAwareChannelResolver;
    @Autowired
    public CommonRepository commonRepository;

    @Autowired
    private AWSClientService awsClientService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TrackRepository trackRepository;

    @StreamListener(value = DelayMsgChannel.DELAY_SERVICE_CONFIRM)
    public void DelayMsgService(DelayMsg delayMsg){
        //根据得到的annotation 获得对应的 vid 的destinations 并处理时间 再返回
        for(Map.Entry<String,List<Destination>>  entry : delayMsg.getDestinationMap().entrySet()){
            String vid = entry.getKey();
            entry.setValue(delayCalculate(vid,delayMsg.getDelaytime()));
        }
        delayMsg.setConnectorType("delayDestinationUpdate");
        org.springframework.messaging.Message<DelayMsg> delayMsgMessage = MessageBuilder.withPayload(delayMsg).setHeader("connectorType", delayMsg.getConnectorType()).build();
        binderAwareChannelResolver.resolveDestination(delayMsg.getConnectorType()).send((org.springframework.messaging.Message<?>) delayMsgMessage);
    }

    public List<Destination> delayCalculate(String vid , String delaytime) {
        IoTClient ioTClient = awsClientService.findDeviceClient(vid);
        VesselDevice vesselDevice = ioTClient.getVesselDevice();
        //TODO: Timing simulation of anchoring and docking status of the ship
        int stepIdx = vesselDevice.getStepIndex();
        long zoomVal = commonRepository.getZoomInVal();
        long simuMs = DateUtil.str2date(vesselDevice.getStartTime()).getTime();
//        Destination curDest = vesselDevice.getDestinations().get(stepIdx);
        long startMs = DateUtil.str2date(vesselDevice.getStartTime()).getTime();
        for(int i = stepIdx ; i < vesselDevice.getDestinations().size(); i++){
            vesselDevice.getDestinations().get(i).setEstiAnchorTime(timeCalculate(delaytime,vesselDevice.getDestinations().get(i).getEstiAnchorTime()));
            vesselDevice.getDestinations().get(i).setEstiArrivalTime(timeCalculate(delaytime,vesselDevice.getDestinations().get(i).getEstiArrivalTime()));
            vesselDevice.getDestinations().get(i).setEstiDepartureTime((timeCalculate(delaytime,vesselDevice.getDestinations().get(i).getEstiDepartureTime())));
        }
        return vesselDevice.getDestinations();
//        while (true) {
//            long curMs = (new Date().getTime() - simuMs) * zoomVal + simuMs;
//            long nextMs = curMs + 1000 * zoomVal;
//            if (vesselDevice.getStatus().equals("Anchoring")) {
//                long newReachMs = DateUtil.str2date(curDest.getEstiArrivalTime()).getTime();
//                logger.debug("Current time : " + DateUtil.ms2dateStr(curMs) + " Next time : " + DateUtil.ms2dateStr(nextMs) + "new reach time : " + curDest.getEstiArrivalTime());
//                if (newReachMs > curMs && newReachMs <= nextMs) {
//                    vesselDevice.updateStatus("Docking");
////                    changeStatus(ioTClient, ioTClient.getUpdateStatusTopic(), "ANCHORING_END", vesselDevice);
//                }
//            } else if (vesselDevice.getStatus().equals("Docking")) {
//                long newDepartureMs = DateUtil.str2date(curDest.getEstiDepartureTime()).getTime();
//                logger.info("Current time : " + DateUtil.ms2dateStr(curMs) + " Next time : " + DateUtil.ms2dateStr(nextMs) + " New arrival time : " + curDest.getEstiDepartureTime());
//                if (newDepartureMs > curMs && newDepartureMs <= nextMs) {
//                    //send depature message to vessel process
////                    changeStatus(ioTClient, ioTClient.getUpdateStatusTopic(), "DOCKING_END", vesselDevice);
//                    logger.info("Docking  , departure");
//                    break;
//                }
//            }
//            int nextStepIndex = stepIdx + 1;
//            vesselDevice.updateStepIndex(nextStepIndex);
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public String timeCalculate(String p,String ct){
        long curTime =  DateUtil.str2date((ct)).getTime();
        long calTime = curTime + DateUtil.str2date((p)).getTime();
        Date cl = new Date();
        cl.setTime(calTime);
        return cl.toString();
    }

}
