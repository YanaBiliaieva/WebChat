package com.course.config;

import com.course.mvc.controller.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import javax.annotation.Resource;
/**
 * Created by Admin on 10.06.2017.
 */
@Configuration
@PropertySource(value = "classpath:websocket.properties")
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Resource
    private Environment env;

    @Bean
    public ChatWebSocketHandler getChatWebSocketHandler(){
        return new ChatWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        System.out.println("IN registerWebSocketHandlers!!!");
        webSocketHandlerRegistry.addHandler(getChatWebSocketHandler(),
                env.getProperty("socket.url")).withSockJS();
    }
}
