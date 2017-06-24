package com.course.mvc.service;

import java.util.Map;

/**
 * Created by Admin on 10.06.2017.
 */
public interface WebSocketService {
    void saveBroadcastMessage(String broadcastMessage, String senderLogin);
    Map<String,String> getMessagesByLogin(String receiverLogin);
    Map<String,String> getBroadcastMessages();

    void savePrivateMessage(String receiverLogin, String senderLogin, String messageToForward);
}
