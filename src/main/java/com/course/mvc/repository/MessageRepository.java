package com.course.mvc.repository;

import com.course.mvc.domain.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Admin on 10.06.2017.
 */
//приватные сообщения
    @Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query("select m from Message m where m.receiver.login=:receiverLogin")
    List<Message> findMessagesByLogin(@Param("receiverLogin") String receiverLogin);
}
