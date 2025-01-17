package com.msc.spring.consumer.jzmq;

import com.msc.spring.consumer.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
@ConditionalOnProperty(prefix = "jzmq", name = "enabled", havingValue = "true")
public class JZMQSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(JZMQSubscriber.class);

    @Value("${zeromq.address}")
    private String bindAddress;

    @Value("${jzmq.enabled}")
    private boolean jzmqEnabled;

    final String errorMessage = "Exception encountered = ";

    @Autowired
    MessageUtils messageUtils;

    @Bean
    @ConditionalOnProperty(prefix = "multi.thread", name = "enabled", havingValue = "false")
    public void consumeJeroMQMessage() {
        if (jzmqEnabled) {
            // Prepare our context and subscriber
            try (ZContext context = new ZContext()) {
                ZMQ.Socket subscriber = context.createSocket(ZMQ.SUB);
                subscriber.connect(bindAddress);
                subscriber.subscribe("B".getBytes());

                LOGGER.info("Starting Subscriber..");
                int i = 0;
                while (true) {
                    // Read envelope with address
                    String messageAddress = subscriber.recvStr();
                    // Read message contents
                    byte[] messageBody = subscriber.recv();
                    messageUtils.saveMessage(messageBody, false);
                    LOGGER.info("Received JZMQ Message for address: " + messageAddress);
                    i++;
                }
            }
        }
    }

    @Bean
    @Async
    @ConditionalOnProperty(prefix = "multi.thread", name = "enabled", havingValue = "true")
    public void consumeJeroMQMessageMultiThread() {
        if (jzmqEnabled) {
            // Prepare our context and subscriber
            try (ZContext context = new ZContext()) {
                ZMQ.Socket subscriber = context.createSocket(ZMQ.SUB);
                subscriber.connect(bindAddress);
                subscriber.subscribe("B".getBytes());

                LOGGER.info("Starting Subscriber..");
                int i = 0;
                while (true) {
                    // Read envelope with address
                    String messageAddress = subscriber.recvStr();
                    // Read message contents
                    byte[] messageBody = subscriber.recv();
                    messageUtils.saveMessage(messageBody, true);
                    LOGGER.info("Received JZMQ Message: '" + messageBody + "'" + "for address: " + messageAddress);
                    i++;
                }
            }
        }
    }

}
