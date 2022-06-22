package org.example.BlogEngine.model;

import javax.persistence.*;


@Entity
@Table(name = "tag_2_post", schema="test")

public class Tag2Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Posts postId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Tags tagId;


    public Tag2Post() {
    }

    public Tag2Post(int id, Posts postId, Tags tagId) {
        this.id = id;
        this.postId = postId;
        this.tagId = tagId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Posts getPostId() {
        return postId;
    }

    public void setPostId(Posts postId) {
        this.postId = postId;
    }

    public Tags getTagId() {
        return tagId;
    }

    public void setTagId(Tags tagId) {
        this.tagId = tagId;
    }
}
