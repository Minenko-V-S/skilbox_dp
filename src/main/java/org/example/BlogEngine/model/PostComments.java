package org.example.BlogEngine.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_comments", schema="test")
public class PostComments {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public PostComments() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private int parentId; // комментарий, на который оставлен этот комментарий (может быть NULL, если комментарий оставлен просто к посту)

    // @Column(nullable = false)
    @ManyToOne()
    @JoinColumn(nullable = false)
    private Posts postId;   // пост, к которому написан комментарий

    @Column(name = "user_id")
    private Integer userId;   // автор комментария

    @Column(nullable = false)
    private Date time;    // дата и время комментария


    public PostComments(Long id, int parentId, Posts postId, Integer userId, Date time) {
        this.id = id;
        this.parentId = parentId;
        this.postId = postId;
        this.userId = userId;
        this.time = time;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Posts getPostId() {
        return postId;
    }

    public void setPostId(Posts postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
