package com.course.mvc.controller;

import com.course.mvc.domain.ChatUser;
import com.course.mvc.listener.HttpSessionCreationListener;
import com.course.mvc.service.WebSocketService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
//Client to Server
    /*{"sessionid":"..."}
    {"broadcast":"Hello"}
    {"login":"Vasya",
    "message":"Hi"}
    {"logout":""}
    //Server to Client
    {"auth":"yes"}
    {"auth":"yes"}
    "list":"["Vasya","Petya"]"}список активных пользователей
    {"auth":"yes"}
    "login":"Vasya",
    "message":"Hi"}
    */

/**
 * Created by Admin on 10.06.2017.
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

    ///String - login клієнта, WebSocketSession - його webSocket сесія
    private Map<String, WebSocketSession> socketSessionMap = new HashMap<>();
    //зберігаються логін і сесія
    private Map<String, String> httpSessionLoginMap = new HashMap<>();
    private WebSocketService socketService;

    @Autowired
    public void setSocketService(WebSocketService socketService) {
        this.socketService = socketService;
        System.out.println("IN CONSTRUCTOR SOCKET SERVICE");
    }

    //визивається коли від клієнта прийшов message
    @Override
    public void handleTextMessage(WebSocketSession socketSession, TextMessage message) throws Exception {
        System.out.println("IN HANDLE TEXT MESSAGE!!");
        ///json -> String
        String jsonMessage = message.getPayload();
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap<String, String>>() {
        }.getType();
        //формуємо map з json
        Map<String, String> stringMap = gson.fromJson(jsonMessage, gsonType);
        System.out.println("SESSION ID "+stringMap.get("sessionid"));
        //if true - user хочет стать online
        if (Objects.nonNull(stringMap.get("sessionid"))) {
            ///регистрируем online
            if (registration(stringMap.get("sessionid"), socketSession)) {
                //registrated online
                socketSession.sendMessage(new TextMessage("{\"auth\":\"yes\"}"));
                ///сказать всем юзерам что online
                sendListOfUsers();
//                System.out.println("LIST OF USER!");
                sendAllMessageForUser(socketSession);
//                System.out.println("AFTER LIST OF USERS!");
            } else {
                //если нету в httpSession юзера с sessionId
                socketSession.sendMessage(new TextMessage("{\"auth\":\"no\"}"));
            }
        } else {
            //1. кто то хочет в обход системи послать json
            //2. пользователь уже online и хочеть послать json
            ///получуаем с sessionSocketMap
            String senderLogin = getKeyByValue(socketSession);
            //true (ключ null) -  уже онлайн
            if (Objects.nonNull(senderLogin)) {
                if (Objects.nonNull(stringMap.get("broadcast"))) {
//                    System.out.println("IN BROADAST!!");
                    //получаем message
                    String broadcastMessage = stringMap.get("broadcast");
//                    System.out.println("MESSAGE: " + broadcastMessage);
                    socketService.saveBroadcastMessage(broadcastMessage,senderLogin);
                    ///формируем json ответа
                    JsonObject broadcastJson = new JsonObject();
                    broadcastJson.addProperty("auth","yes");
                    broadcastJson.addProperty("name", senderLogin);
                    broadcastJson.addProperty("message",broadcastMessage);
                    ///отсилает сообщ. всем активним пользователям
                    sendAllActiveUsers(broadcastJson);

                } else if (Objects.nonNull(stringMap.get("login"))) { //приватные сообщения если ключ login
                    String receiverLogin = stringMap.get("login");
                    String messageToForward = stringMap.get("message");
                    if (Objects.nonNull(receiverLogin)) {
                        //user active
                        forwardMessage(receiverLogin, senderLogin, messageToForward);
                    } else {
                        //если user offline
                        saveMessageToDB(receiverLogin, senderLogin, messageToForward);
                    }
                } else if (Objects.nonNull(stringMap.get("logout"))) {//хочет уйти из чата
                    ///убиваем session
                    invalidateHttpSession(socketSession);
                    ///удаляем user из активных
                    removeUserFromMap(socketSession);
                } else {
                    socketSession.sendMessage(new TextMessage("bad json"));
                }
            } else {
                socketSession.sendMessage(new TextMessage("{\"auth\":\"no\"}"));
            }

        }
    }

    private boolean registration(String sessionId, WebSocketSession socketSession) {
        ///по sessionID получить session, проверить, а потом добавиться socketSession в map

        HttpSession httpSession = HttpSessionCreationListener.getSessionById(sessionId);
        ///проверям пользователя на что он залогинен
        if (Objects.nonNull(httpSession.getAttribute("user"))) {
            ChatUser chatUser = (ChatUser) httpSession.getAttribute("user");
            String login = chatUser.getLogin();
            socketSessionMap.put(login, socketSession);
            httpSessionLoginMap.put(login, sessionId);
            return true;
        }

        return false;
    }

    private void sendListOfUsers() throws IOException {
        Gson gson = new Gson();
        Set<String> activeUsersKeys = socketSessionMap.keySet();
        JsonObject listActiveUsers = new JsonObject();
        listActiveUsers.addProperty("auth", "yes");
        listActiveUsers.add("list", gson.toJsonTree(activeUsersKeys));
        for (WebSocketSession session : socketSessionMap.values()) {
            session.sendMessage(new TextMessage(listActiveUsers.toString()));
        }

    }

    private void sendAllMessageForUser(WebSocketSession socketSession) throws IOException {
        String receiverLogin = getKeyByValue(socketSession);
        //от кого сообщение и само сообщение
        Map<String,String> messages = socketService.getMessagesByLogin(receiverLogin);
        for (Map.Entry<String,String> entry: messages.entrySet()) {
            sendMessage(socketSession, entry);
        }
//        System.out.println("IN SEND ALL MESSAGE!");
        Map<String,String> broadcastMessages = socketService.getBroadcastMessages();
        for (Map.Entry<String,String> entry: broadcastMessages.entrySet()) {
            sendMessage(socketSession, entry);
        }
    }

    private void sendMessage(WebSocketSession socketSession, Map.Entry<String, String> entry) throws IOException {
        JsonObject message = new JsonObject();
        message.addProperty("auth", "yes");
        message.addProperty("login",entry.getKey());
        message.addProperty("message",entry.getValue());
        socketSession.sendMessage(new TextMessage(message.toString()));
    }

    private String getKeyByValue(WebSocketSession socketSession) {
        for (Map.Entry<String,WebSocketSession> entry:socketSessionMap.entrySet()) {
            if(entry.getValue()==socketSession){
                return entry.getKey();
            }
        }
        return null;
    }

    private void sendAllActiveUsers(JsonObject broadcastJson) throws IOException {
        for (WebSocketSession session : socketSessionMap.values()) {
            session.sendMessage(new TextMessage(broadcastJson.toString()));
        }
        System.out.println("IN sendAllActiveUsers");
    }

    private void forwardMessage(String receiverLogin, String senderLogin, String messageToForward) throws IOException {
        Gson gson = new Gson();
        JsonObject privateMessage = new JsonObject();
        privateMessage.addProperty("auth", "yes");
        privateMessage.addProperty("login",senderLogin);
        privateMessage.addProperty("message",messageToForward);
        socketSessionMap.get(receiverLogin).sendMessage(new TextMessage(privateMessage.toString()));
    }

    private void saveMessageToDB(String receiverLogin, String senderLogin, String messageToForward) {
        socketService.savePrivateMessage(receiverLogin,senderLogin,messageToForward);
    }

    private void invalidateHttpSession(WebSocketSession socketSession) {
        String login = getKeyByValue(socketSession);
        String sessionId = httpSessionLoginMap.get(login);
        httpSessionLoginMap.remove(login);
        HttpSession httpSession = HttpSessionCreationListener.getSessionById(sessionId);
        httpSession.invalidate();
    }

    private void removeUserFromMap(WebSocketSession socketSession) {
        String login = getKeyByValue(socketSession);
        socketSessionMap.remove(login);
    }

}
