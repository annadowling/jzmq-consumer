package com.msc.spring.consumer.jzmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
@ConditionalOnProperty(prefix = "jzmq", name = "enabled", havingValue = "true")
public class JZMQSubscriber {

    @Value("${zeromq.address}")
    private String bindAddress;

    @Value("${jzmq.enabled}")
    private boolean jzmqEnabled;

    final String errorMessage = "Exception encountered = ";

    @Bean
    public void consumeJZMQMessage() {
        if (jzmqEnabled) {
            // Prepare our context and subscriber
            try {
                ZMQ.Context context = ZMQ.context(1);
                ZMQ.Socket subscriber = context.socket(ZMQ.SUB);

                subscriber.connect(bindAddress);
                subscriber.subscribe("B".getBytes());

                System.out.println("Starting Subscriber..");
                int i = 0;
                while (true) {
                    String address = subscriber.recvStr();
                    String contents = subscriber.recvStr();
                    System.out.println(address + ":" + new String(contents) + ": " + (i));
                    i++;
                }
            } catch (Exception e) {
                System.out.println(errorMessage + e.getLocalizedMessage());
            }
        }
    }
}