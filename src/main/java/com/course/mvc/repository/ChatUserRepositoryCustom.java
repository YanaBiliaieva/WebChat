package com.course.mvc.repository;

import com.course.mvc.domain.ChatUser;
import com.course.mvc.dto.ChatUserDto;
/**
 * Created by Admin on 27.05.2017.
 */
public interface ChatUserRepositoryCustom {
    void saveUser(ChatUser user);
}
