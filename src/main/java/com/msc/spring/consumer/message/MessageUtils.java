package com.msc.spring.consumer.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtils.class);

    @Autowired
    MessageRepository messageRepository;

    /**
     * Convert a byte array to an Object
     *
     * @param bytes
     * @return
     */
    public Object byte2Object(byte[] bytes) {
        Object obj = new Object();

        ObjectInputStream bin;
        try {
            bin = new ObjectInputStream(new ByteArrayInputStream(bytes));
            obj = bin.readObject();
        } catch (IOException e) {
            LOGGER.info("Unable to convert bytes: " + e);
        } catch (ClassNotFoundException e) {
            LOGGER.info("Unable to convert bytes: " + e);
        }

        return obj;
    }

    /**
     * Save a message entry to the db for each received message
     *
     * @param bytes
     */
    public void saveMessage(byte[] bytes, boolean isMultiThreaded) {
        if (bytes.length > 1) {
            Message message = new Message();
            message.setReceiveTime(new Date());
            Map<String, String> receivedObject = (HashMap<String, String>) byte2Object(bytes);
            message.setCorrelationId(receivedObject.get("correlationId"));
            message.setRequestType(receivedObject.get("messageId"));
            message.setMessageVolume(Integer.parseInt(receivedObject.get("messageVolume")));
            message.setMessageSize(Integer.parseInt(receivedObject.get("messageSize")));
            message.setMultiThreaded(isMultiThreaded);

            messageRepository.save(message);
        }
    }
}
