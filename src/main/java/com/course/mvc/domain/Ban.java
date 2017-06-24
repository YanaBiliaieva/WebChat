package com.course.mvc.domain;

import javax.persistence.*;

/**
 * Created by Admin on 20.05.2017.
 */
@Entity
@Table(name="bans")
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private ChatUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatUser getUser() {
        return user;
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }

}
