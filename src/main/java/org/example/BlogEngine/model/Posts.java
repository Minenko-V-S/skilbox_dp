package org.example.BlogEngine.model;

import lombok.NoArgsConstructor;
import org.example.BlogEngine.enums.ModerationStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "posts", schema="test")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int PostId;

    @Column(name = "is_active")
    private boolean isActive;       // скрыта или активна публикация: 0 или 1

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')")
    private ModerationStatus moderationStatus;  // статус модерации, по умолчанию значение NEW

    @Column(name = "moderator_id")
    private int moderatorId;        // ID пользователя-модератора, принявшего решение, или NULL

    @Column(name = "user_id")
    private Integer userId;             // автор поста

    private Timestamp timestamp;   // дата и время публикации поста
    private String title;           // заголовок поста
    private String text;            // текст поста

    @Column(name = "view_count")
    private int viewCount;          // количество просмотров поста

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL)
    private List<PostComments> commentsList;


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public List<PostComments> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<PostComments> commentsList) {
        this.commentsList = commentsList;
    }

    public String getAnnounce() {
        String postText = getText();
        if( postText == null){
            return "";
        }

        String announce = postText.replaceAll("<(.*?)>","" ).replaceAll("[\\p{P}\\p{S}]", "");
        announce = announce.substring(0, Math.min(150, announce.length())) + "...";
        return announce;
    }
}
