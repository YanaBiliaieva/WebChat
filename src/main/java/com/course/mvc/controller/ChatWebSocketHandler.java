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
    //String - login, WebSocketSession- его WebSocket сессия
    private Map<String, WebSocketSession> socketSessionMap = new HashMap<>();//хранится логин клиента и соотв ему вебсокут сессия
    private Map<String, String> httpSessionLoginMap = new HashMap<>();//хранится логин и идентификатор сессии
    private WebSocketService socketService;

    @Autowired
    private void setSocketService(WebSocketService socketService) {
        this.socketService = socketService;
    }

    @Override
    public void handleTextMessage(WebSocketSession socketSession, TextMessage message) throws Exception {
        //json->String
        //вызывается когда от клиента пришло сообщение
        //это вебсокет сессия. связанная с клиентом.
        String jsonMessage = message.getPayload();
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap<String, String>>() {//парсим, получаем мэп
        }.getType();//пустой анонимный класс-наледник TypeToken. указываем какого типа бужет значение
        Map<String, String> stringMap = gson.fromJson(jsonMessage, gsonType);
        // String key=stringMap.keySet().iterator().next();
        if (Objects.nonNull(stringMap.get("sessionid")))
        {//тру - решаем, что это новый клиент, который хочет зарегистрироваться онлайн
            if (registration(stringMap.get("sessionid"), socketSession)) {//регистрируем его
                socketSession.sendMessage(new TextMessage("{'auth':'yes'}"));//если зарег - отсылаем ему ейс
                sendListOfUsers();//все онлайн пользователям отсылаем новый списко клиентов
                sendAllMessageForUser(socketSession);//отсылаем что пришло в его  отсутствие
            } else {
                socketSession.sendMessage(new TextMessage("{'auth':'no'}"));//не зарегистрировали и ничего не отсылаем ему
            }
        } else{
            //проверяем sessionid==null
            String senderLogin = getKeyByValue(socketSession);//если сессион айди нал
            // 1) кто-то не зарегистрировался или не онлайн и хочет послать джисон
            // 2) пользователь уже онлайн и хочет посылать джисок
            if (Objects.nonNull(senderLogin)) {
                if (Objects.nonNull(stringMap.get("broadcast"))) {//если пользователь прислал всем
                    String broadcastMessage = stringMap.get("broadcast");//получаем сообщ
                    socketService.saveBroadcastMessage(broadcastMessage,senderLogin);//записываем в реддис
                    JsonObject broadcastJson = new JsonObject();//формируем джисон ответ
                    broadcastJson.addProperty(senderLogin, broadcastMessage);

                    sendAllActiveUsers(broadcastJson);//отослать этот джисон всем активным пользователям

                } else if (Objects.nonNull(stringMap.get("login"))) {//пользователь с логином прислал приватное сообщение
                    String receiverLogin = stringMap.get("login");//получаем логин для кого сообщение
                    String messageToForward = stringMap.get(("message"));//
                    if (Objects.nonNull(receiverLogin)) {//если не активен пользователь
                        forwardMessage(receiverLogin, senderLogin, messageToForward);//отсылаем ему сообщ, в бд не сохраняем
                    } else {
                        saveMessageToDB(receiverLogin, senderLogin, messageToForward);//сохраняем сообщение в постгрес бд, если пользователь офлайн
                    }

                } else if (Objects.nonNull(stringMap.get("logout"))) {//пользователь нажал кнопку логаут и хочет выйти из чата
                    invalidateHttpSession(socketSession);   //делаем неактивной сессию
                    removeUserFromMap(socketSession);//выбрасываем его из первого мэпа, из активных пользователей

                } else {
                    socketSession.sendMessage(new TextMessage("bad json"));
                }//пришло что-то непонятное
            } else {
                socketSession.sendMessage(new TextMessage("{'auth':'no'}"));
            }
        }
    }

    private boolean registration(String sessionId, WebSocketSession socketSession) { //получить сессию, проверить ее, добавить сокет сешн в соответствуюший мэп
        HttpSession httpSession = HttpSessionCreationListener.getSessionById(sessionId);
        //проверяем что пользователь,который хочет спойти в чат, залогинился
        // получаем логин пользователя
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
        String receiverLogin=getKeyByValue(socketSession);
        //от кого сообщение и само сообщение
        Map<String,String> messages =socketService.getMessagesByLogin(receiverLogin);
        sendMessage(socketSession, messages);
        Map<String,String> broadcastMessages=socketService.getBroadcastMessages();
        sendMessage(socketSession, broadcastMessages);
    }

    private void sendMessage(WebSocketSession socketSession, Map<String, String> broadcastMessages) throws IOException {
        for (Map.Entry<String,String> entry:broadcastMessages.entrySet()) {
            JsonObject message = new JsonObject();
            message.addProperty("login",entry.getValue());
            message.addProperty("message",entry.getValue());
            socketSession.sendMessage((new TextMessage(message.toString())));
        }
    }
    private  String getKeyByValue(WebSocketSession socketSession){
        for (Map.Entry<String,WebSocketSession> entry:socketSessionMap.entrySet()) {
            if(entry.getValue()==socketSession){
                return entry.getKey();
            }
        }return null;
    }
    private void sendAllActiveUsers(JsonObject broadcastJson) throws IOException {
        for (WebSocketSession session : socketSessionMap.values()) {
            session.sendMessage(new TextMessage(broadcastJson.toString()));
        }
    }
    private void forwardMessage(String receiverLogin, String senderLogin, String messageToForward) throws IOException {
        Gson gson = new Gson();
        JsonObject privateMessage = new JsonObject();
        privateMessage.addProperty("auth", "yes");
        privateMessage.addProperty("login",senderLogin);
        privateMessage.addProperty("message",messageToForward);
        socketSessionMap.get(receiverLogin).sendMessage(new TextMessage(privateMessage.toString()));
    }
    private void saveMessageToDB(String receiveLogin,String senderLogin, String messageToForward){
        socketService.savePrivateMessage(receiveLogin,senderLogin,messageToForward);
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
