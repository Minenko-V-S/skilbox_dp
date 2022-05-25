package org.example.BlogEngine.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "users")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private  boolean isModerator; // является ли пользователь модератором

    @Column(nullable = false)
    private Date regTime;         // дата и время регистрации пользователя

    @Column(nullable = false)
    private String name;          // имя пользователя

    @Column(nullable = false)
    private String email;         // e-mail пользователя

    @Column(nullable = false)
    private String password;      // хэш пароля пользователя

    private String code;          // код для восстановления пароля

    private String photo;         // фотография (ссылка на файл)

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<Posts> postList;

}
