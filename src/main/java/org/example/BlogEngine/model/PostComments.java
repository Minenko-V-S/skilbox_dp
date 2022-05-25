package org.example.BlogEngine.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_comments", schema="test")
@NoArgsConstructor
public class PostComments {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Users userId;   // автор комментария

    @Column(nullable = false)
    private Date time;    // дата и время комментария


    public PostComments(long id, int parentId, Posts postId, Users userId, Date time) {
        this.id = id;
        this.parentId = parentId;
        this.postId = postId;
        this.userId = userId;
        this.time = time;
    }
}
