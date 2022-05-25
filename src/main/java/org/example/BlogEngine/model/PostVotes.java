package org.example.BlogEngine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "post_votes", schema="test")
@Data
@NoArgsConstructor
public class PostVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Users userId;  // тот, кто поставил лайк / дизлайк

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts postId;  // пост, которому поставлен лайк / дизлайк

    @Column(nullable = false)
    private Date time;   // дата и время лайка / дизлайка

    @Column(nullable = false)
    private byte value;  //  лайк или дизлайк: 1 или -1
}
