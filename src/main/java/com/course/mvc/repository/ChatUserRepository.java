package com.course.mvc.repository;
import com.course.mvc.domain.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Created by Admin on 27.05.2017.
 */
@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser,Long>,ChatUserRepositoryCustom {

public ChatUser findChatUserByLogin(String login);

}