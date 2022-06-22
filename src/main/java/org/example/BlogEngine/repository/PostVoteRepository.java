package org.example.BlogEngine.repository;

import org.example.BlogEngine.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository <PostVotes, Integer> {

    @Query("SELECT count(pv) FROM PostVotes pv WHERE pv.postId = ?1 AND pv.value = ?2")
    Optional<Integer> findCountVotesByPostId (int postId, int value);
}
