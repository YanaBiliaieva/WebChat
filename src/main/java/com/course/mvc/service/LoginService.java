package com.course.mvc.service;

import com.course.mvc.domain.ChatUser;
import com.course.mvc.dto.ChatUserDto;

/**
 * Created by Admin on 27.05.2017.
 */
public interface LoginService {
    void save(ChatUserDto chatUserDto);
     ChatUser verifyLogin(String login, String password);
}
