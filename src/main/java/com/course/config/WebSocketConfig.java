package com.course.config;

import com.course.mvc.controller.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by Admin on 10.06.2017.
 */
@Configuration
@EnableWebSocket
@PropertySource(value = "classpath:websocket.properties")
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{
    @Bean
    public ChatWebSocketHandler getChatWebSocketHandler(){
        return new ChatWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {

        webSocketHandlerRegistry.addHandler(getChatWebSocketHandler(), "/socket.url").withSockJS();
    }
}
