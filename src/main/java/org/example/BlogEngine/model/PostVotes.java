package org.example.BlogEngine.model;

import lombok.Data;


import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "post_votes", schema="test")
public class PostVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int voteId;

    @Column(name = "user_id")
    private Integer userId;  // тот, кто поставил лайк / дизлайк

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts postId;  // пост, которому поставлен лайк / дизлайк

    @Column(nullable = false)
    private Timestamp time;   // дата и время лайка / дизлайка

    @Column(nullable = false)
    private Integer value;  //  лайк или дизлайк: 1 или -1

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Posts getPostId() {
        return postId;
    }

    public void setPostId(Posts postId) {
        this.postId = postId;
    }

    public Timestamp getTime() {
        return time;
    }

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
