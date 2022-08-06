package org.example.BlogEngine.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_comments", schema="test")
public class PostComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer commentId;

    private Integer parent_id;

    @Column(insertable = false, updatable = false)
    private Integer post_id;

    @Column(name = "user_id")
    private Integer userId;
    private Timestamp timestamp;
    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="post_id")
    public Posts post;

    public PostComments() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostComments )) return false;
        return commentId != null && commentId.equals(((PostComments) o).getCommentId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getTime() {
        return timestamp;
    }

    public void setTime(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }
}
