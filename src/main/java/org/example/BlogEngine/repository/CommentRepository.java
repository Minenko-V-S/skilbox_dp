package org.example.BlogEngine.repository;

import org.example.BlogEngine.model.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComments, Integer> {

    List<PostComments> findCommentsByPostId(int post_id);

    int getCommentCountByPostId(int post_id);
}