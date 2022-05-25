package org.example.BlogEngine.model;

import lombok.NoArgsConstructor;
import org.example.BlogEngine.enums.ModerationStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "posts", schema="test")
@NoArgsConstructor
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private boolean isActive;       // скрыта или активна публикация: 0 или 1

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')")
    private ModerationStatus moderationStatus;  // статус модерации, по умолчанию значение NEW

    @Column(nullable = false)
    private int moderatorId;        // ID пользователя-модератора, принявшего решение, или NULL

    @ManyToOne()
    @JoinColumn(nullable = false, referencedColumnName="id")
    private Users userId;             // автор поста

    @Column(nullable = false)
    private Date time;              // дата и время публикации поста

    @Column(nullable = false)
    private String title;           // заголовок поста

    @Column(nullable = false)
    private String text;            // текст поста

    @Column(nullable = false)
    private int viewCount;          // количество просмотров поста

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL)
    private List<PostComments> commentsList;




    public Posts(int id, boolean isActive, ModerationStatus moderationStatus, int moderatorId, Users userId, Date time, String title, String text, int viewCount) {
        this.id = id;
        this.isActive = isActive;
        this.moderationStatus = moderationStatus;
        this.moderatorId = moderatorId;
        this.userId = userId;
        this.time = time;
        this.title = title;
        this.text = text;
        this.viewCount = viewCount;
    }


}
