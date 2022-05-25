package org.example.BlogEngine.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Entity
@Table(name = "tag_2_post", schema="test")
@Data
@NoArgsConstructor
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



    public Tag2Post(int id, Posts postId, Tags tagId) {
        this.id = id;
        this.postId = postId;
        this.tagId = tagId;
    }
}
