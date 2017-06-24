package com.course.mvc.service.impl;
import com.course.mvc.domain.ChatUser;
import com.course.mvc.domain.Role;
import com.course.mvc.domain.RoleEnum;
import com.course.mvc.dto.ChatUserDto;
import com.course.mvc.exceptions.ServiceException;
import com.course.mvc.exceptions.UserSaveException;
import com.course.mvc.repository.ChatUserRepository;
import com.course.mvc.repository.RoleRepository;
import com.course.mvc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

/**
 * Created by Admin on 27.05.2017.
 */
@Service
public class LoginServiceImpl implements LoginService {

    private ChatUserRepository chatUserRepository;
    private RoleRepository roleRepository;

    @Autowired
    public LoginServiceImpl(ChatUserRepository chatUserRepository, RoleRepository roleRepository) {
        this.chatUserRepository = chatUserRepository;
        this.roleRepository=roleRepository;
    }

    @Override
//    @Transactional
    public void save(ChatUserDto chatUserDto) {
        Role role=roleRepository.findRoleByName(RoleEnum.USER);
        ChatUser chatUser = new ChatUser.Builder()
                .setName(chatUserDto)
                .setLogin(chatUserDto)
                .setRole(role)
                .setPassword(chatUserDto)
                .build();
        try {
            System.out.println("IN_LOGIN_SERVICE!");
            chatUserRepository.saveUser(chatUser);
        }catch (UserSaveException ex){
            ///intercept by aspectExceptionHandler
            throw new ServiceException(ex.getMessage());
        }
    }
    @Override

    public ChatUser verifyLogin(String login, String password){
        ChatUser user=chatUserRepository.findChatUserByLogin(login);
        if(Objects.nonNull(user)&&user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
}

