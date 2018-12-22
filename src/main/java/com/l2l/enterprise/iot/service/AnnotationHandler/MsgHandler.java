package com.l2l.enterprise.iot.service.AnnotationHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
    public void DelayMsgService(DelayMsg delayMsg) throws JsonProcessingException {
        //根据得到的annotation 获得对应的 vid 的destinations 并处理时间 再返回
        for(Map.Entry<String,List<Destination>>  entry : delayMsg.getDestinationMap().entrySet()){
            String vid = entry.getKey();
            entry.setValue(delay(vid,delayMsg.getDelayx(),delayMsg.getDelayy(),entry.getValue()));
        }
        delayMsg.setConnectorType("delayDestinationUpdate");
        org.springframework.messaging.Message<DelayMsg> delayMsgMessage = MessageBuilder.withPayload(delayMsg).setHeader("connectorType", delayMsg.getConnectorType()).build();
        binderAwareChannelResolver.resolveDestination(delayMsg.getConnectorType()).send((org.springframework.messaging.Message<?>) delayMsgMessage);
    }

//    public List<Destination> delayCalculate(String vid , String delaytime , List<Destination> destinationList) {
//        IoTClient ioTClient = awsClientService.findDeviceClient(vid);
//        VesselDevice vesselDevice = ioTClient.getVesselDevice();
//        //TODO: Timing simulation of anchoring and docking status of the ship
//        int stepIdx = vesselDevice.getStepIndex();
//        long zoomVal = commonRepository.getZoomInVal();
//        long simuMs = DateUtil.str2date(vesselDevice.getStartTime()).getTime();
////        Destination curDest = vesselDevice.getDestinations().get(stepIdx);
//        long startMs = DateUtil.str2date(vesselDevice.getStartTime()).getTime();
//        for(int i = stepIdx ; i < vesselDevice.getDestinations().size(); i++){
//            destinationList.get(i).setEstiAnchorTime(timeCalculate(delaytime,vesselDevice.getDestinations().get(i).getEstiAnchorTime()));
//            destinationList.get(i).setEstiArrivalTime(timeCalculate(delaytime,vesselDevice.getDestinations().get(i).getEstiArrivalTime()));
//            destinationList.get(i).setEstiDepartureTime((timeCalculate(delaytime,vesselDevice.getDestinations().get(i).getEstiDepartureTime())));
//            //模拟给iot发送更改destinations
//            vesselDevice.getDestinations().get(i).setEstiAnchorTime(destinationList.get(i).getEstiAnchorTime());
//            vesselDevice.getDestinations().get(i).setEstiArrivalTime(destinationList.get(i).getEstiArrivalTime());
//            vesselDevice.getDestinations().get(i).setEstiDepartureTime(destinationList.get(i).getEstiDepartureTime());
//
//        }
//        return destinationList;
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
//    }

    public String timeCalculate(String p,String ct){
        long curTime =  DateUtil.str2date((ct)).getTime();
        long calTime = curTime + DateUtil.str2date((p)).getTime();
        Date cl = new Date();
        cl.setTime(calTime);
        return cl.toString();
    }


    public List<Destination> delay(String vid,String delayx, String delayy, List<Destination> destinations) throws JsonProcessingException {
//        logger.info("--POST /{pid}/delay--"+pid+"--"+mp);
//        int dx = Integer.parseInt(mp.get("dx").toString());
//        int dy = Integer.parseInt(mp.get("dy").toString());
        //TODO:modify vesselShadow and sync shadow to device
//        Map<java.lang.String, Object> pvars = runtimeService.getVariables(pid);
//        java.lang.String vid = pvars.get("vid").toString();
        int dx = Integer.parseInt(delayx);
        int dy = Integer.parseInt(delayy);

//        VesselShadow vesselShadow = shadowRepository.findById(vid);
//        if (vesselShadow == null) {
//            return new ResponseEntity<java.lang.String>("{\"Tips\":\"There is no corresponding process instance for this vessel identifier!\"" , HttpStatus.OK);
//        }
//        List<Destination> destinations = vesselShadow.getDestinations();
        logger.info(destinations.toString());
        //TODO : find current port
        IoTClient ioTClient = awsClientService.findDeviceClient(vid);
        VesselDevice vesselDevice = ioTClient.getVesselDevice();
        int curPortIndex = vesselDevice.getStepIndex();
        //TODO : find current time
        long zoomVal = commonRepository.getZoomInVal();
        long simuMs = DateUtil.str2date(vesselDevice.getStartTime()).getTime();
        long curMs = (new Date().getTime()-simuMs)*zoomVal+simuMs;
        //TODO : when status is "Voyaging" or "Anchoring"
        if(vesselDevice.getStatus().equals("Voyaging") || vesselDevice.getStatus().equals("Anchoring")) {
            logger.debug("when status is \"Voyaging\" or \"Anchoring\"");
            long gapMs = -1;
            for (int i = 0; i < destinations.size(); i++) {
                Destination d = destinations.get(i);
                java.lang.String newEstiAnchoringTime = null;
                java.lang.String newEstiArrivalTime = null;
                java.lang.String newEstiDepartureTime = null;

                if (i == curPortIndex) {
                    newEstiAnchoringTime = d.getEstiAnchorTime();//EstiAnchoringTime 不变
                    newEstiArrivalTime = DateUtil.date2str(DateUtil.
                        transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + dx * 60 * 60 * 1000));

                    //TODO:EstiArrivalTime + delay > currentTime --> New EstiArrivalTime must be later than the current time.
                    if(DateUtil.str2date(newEstiArrivalTime).getTime() < curMs) {
                        logger.debug("New estimate arrival time is illegal ,  required to be later than current time");
//                        return new ResponseEntity<java.lang.String>("{\"Tips\":\"New estimate arrival time is illegal ,  required to be later than current time.\"" , HttpStatus.OK);
                    }
                    gapMs = (dx+dy) * 60 * 60 * 1000;
                    newEstiDepartureTime = DateUtil.date2str(DateUtil.
                        transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
                    //TODO:NewEstiDepartureTime > NewEstiArrivalTime --> NewEstiDepartureTime must be later than the NewEstiArrivalTime.
                    if(DateUtil.TimeMinus(newEstiDepartureTime , newEstiArrivalTime) < 0) {
                        logger.debug("New estimate departure time is illegal ,  required to be later than estimate arrival time");
//                        return new ResponseEntity<java.lang.String>("{\"Tips\":\"New estimate departure time is illegal ,  required to be later than estimate arrival time.\"" , HttpStatus.OK);
                    }
                }

                if (i > curPortIndex) {
                    newEstiAnchoringTime = DateUtil.date2str(DateUtil
                        .transForDate(DateUtil.str2date(d.getEstiAnchorTime()).getTime() + gapMs));
                    newEstiArrivalTime = DateUtil.date2str(DateUtil
                        .transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + gapMs));
                    newEstiDepartureTime = DateUtil.date2str(DateUtil
                        .transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
                }
                //TODO: update destination of shadow
                d.setEstiAnchorTime(newEstiAnchoringTime);
                d.setEstiArrivalTime(newEstiArrivalTime);
                d.setEstiDepartureTime(newEstiDepartureTime);
            }

            //TODO : send destinations to vessel device.
            logger.debug("send destinations to vessel device.");
            vesselDevice.updateDestinations(destinations);
//            restClient.postDestinations(destinations , vid);

            //TODO: notify logistic of "Planning"
//            HashMap<java.lang.String, Object> msgBody = new HashMap<java.lang.String, Object>();
//            msgBody.put("eventType" , "DELAY");
//            msgBody.put("phase" , vesselDevice.getStatus());
//            msgBody.put("dx" , dx);
//            msgBody.put("dy" , dy);
//            restClient.notifyMsg(pid , "Planning" , msgBody);

            java.lang.String anchoringMsg = "";
            java.lang.String dockingMsg = "";
            java.lang.String msg ="The ship will";
            if(dx != 0){
                if(dx > 0){
                    anchoringMsg = " arrive the port port after a delay of "+ dy +" hours";
                }else{
                    anchoringMsg = " arrive the port " + (-dy) + " hours in advance";
                }

                msg += anchoringMsg;
            }
            if(dy != 0){
                if(!msg.equals("The ship will")){
                    msg+= " and";
                }
                if(dy > 0){
                    dockingMsg = " leave the port port after a delay of "+ dy +" hours";
                }else{
                    dockingMsg = " leave the port " + (-dy) + " hours in advance";
                }
                msg+=dockingMsg;
            }

            if(!msg.equals("The ship will")){
//                stompClient.sendDelayMsg("admin" , "/topic/vessel/delay" , pid , msg);
                logger.debug(msg);
            }
            //TODO : when status is "Docking"
        } else if(vesselDevice.getStatus().equals("Docking")) {
            logger.debug("when status is \"Docking\"");
            //TODO: can not set dy
            if(dx != 0){
                dx = 0;
            }
            long gapMs = 0;
            for(int i = 0 ; i < destinations.size();i++){
                java.lang.String newEstiAnchoringTime = null;
                java.lang.String newEstiArrivalTime = null;
                java.lang.String newEstiDepartureTime = null;
                Destination d = destinations.get(i);
                if(i == curPortIndex ){
                    newEstiAnchoringTime = d.getEstiAnchorTime();
                    newEstiArrivalTime = d.getEstiArrivalTime();
                    newEstiDepartureTime = DateUtil.date2str(DateUtil.
                        transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + dy * 60 * 60 * 1000));
                    gapMs = dy * 60 * 60 * 1000;
                    //TODO: determine if the departure time is later than cur Ms
                    if(DateUtil.str2date(newEstiDepartureTime).getTime() < curMs) {
                        logger.debug("New estimate departure time is illegal : required to be later than current time.");
//                        return new ResponseEntity<java.lang.String>("{\"Tips\":\"New estimate departure time is illegal : required to be later than current time.\"" , HttpStatus.OK);
                    }

                }

                if(i > curPortIndex) {
                    newEstiAnchoringTime = DateUtil.date2str(DateUtil
                        .transForDate(DateUtil.str2date(d.getEstiAnchorTime()).getTime() + gapMs));
                    newEstiArrivalTime = DateUtil.date2str(DateUtil
                        .transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + gapMs));
                    newEstiDepartureTime = DateUtil.date2str(DateUtil
                        .transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
                }

                //TODO: update destination of shadow
                d.setEstiAnchorTime(newEstiAnchoringTime);
                d.setEstiArrivalTime(newEstiArrivalTime);
                d.setEstiDepartureTime(newEstiDepartureTime);
            }
            //TODO : send destinations to vessel device.
            logger.info("send destinations to vessel device.");
            vesselDevice.updateDestinations(destinations);

            //TODO: notify logistic of "Planning"
//            HashMap<java.lang.String, Object> msgBody = new HashMap<java.lang.String, Object>();
//            msgBody.put("eventType" , "DELAY");
//            msgBody.put("phase" , "Docking");
//            msgBody.put("dy" , dy);
//            restClient.notifyMsg(pid , "Planning" , msgBody);
//            java.lang.String msg = "";
//            if(dy != 0){
//                if(dy > 0){
//                    msg = "The ship will leave the port port after a delay of "+ dy +" hours";
//                }else{
//                    msg = "The ship will leave the port " + (-dy) + " hours in advance";
//                }
////                stompClient.sendDelayMsg("admin" , "/topic/vessel/delay" , pid , msg);
//                logger.debug(msg);
//            }
        } else{
            logger.debug("The current situation is not considered!(the current status of the vessel is ignored)");
//            return new ResponseEntity<java.lang.String>("{\"Tips\":\"The current situation is not considered!(the current status of the vessel is ignored\")", HttpStatus.OK);
        }

        // delay/postpone event to coordinator
//        return new ResponseEntity<java.lang.String>("{\"Tips\":\"ok\"}" , HttpStatus.OK);
        return destinations;
    }

}
