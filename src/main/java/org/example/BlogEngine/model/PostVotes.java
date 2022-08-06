package org.example.BlogEngine.model;

import lombok.Data;


import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "post_votes", schema="test")
public class PostVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer voteId;

    @Column(name = "post_id", insertable = false, updatable = false)
    private Integer postId;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts post;

    private Timestamp time;
    private Integer value;

    public PostVotes() {
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getVoteId() {
        return voteId;
    }

    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public Timestamp getTime() {return time;}

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
