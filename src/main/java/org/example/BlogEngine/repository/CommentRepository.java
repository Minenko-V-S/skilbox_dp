package org.example.BlogEngine.repository;

import org.example.BlogEngine.model.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComments, Integer> {

    @Query("FROM PostComments pc WHERE pc.post_id = ?1")
    List<PostComments> findCommentsByPostId (int post_id);

    @Query("SELECT count(pc) FROM PostComments pc WHERE pc.post_id = ?1")
    int getCommentCountByPostId(int post_id);
}