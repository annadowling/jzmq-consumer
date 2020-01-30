package com.msc.spring.consumer.message;

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

    @Autowired
    MessageRepository messageRepository;

    /**
     * Convert a byte array to an Object
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
            System.out.println("Unable to convert bytes to ArrayList<String> " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to convert bytes to ArrayList<String> " + e);
        }

        return obj;
    }

    /**
     * Save a message entry to the db for each received message
     * @param bytes
     */
    public void saveMessage(byte[] bytes){
        Message message = new Message();
        message.setReceiveTime(new Date());
        Map<String, String> receivedObject = (HashMap<String, String>) byte2Object(bytes);
        message.setCorrelationId(receivedObject.get("correlationId"));
        message.setRequestType(receivedObject.get("messageId"));
        message.setMessageVolume(Integer.parseInt(receivedObject.get("messageVolume")));
        message.setMessageSize(Integer.parseInt(receivedObject.get("messageSize")));

        messageRepository.save(message);
    }
}
