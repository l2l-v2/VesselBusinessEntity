package com.l2l.enterprise.iot.channel;

import afu.org.checkerframework.checker.igj.qual.I;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface DelayMsgChannel {
    String DELAY_MSG = "delaymsgConsumer";

    String DELAY_SERVICE_CONFIRM = "delayServiceConfirm";

    String DELAY_DESTINATION_UPDATE = "delayDestinationUpdate";
    @Output("delaymsgConsumer")
    MessageChannel delaymsgConsumer();

    @Input("delayServiceConfirm")
    SubscribableChannel delayServiceConfirm();

    @Output("delayDestinationUpdate")
    MessageChannel delayDestinationUpdate();

}
