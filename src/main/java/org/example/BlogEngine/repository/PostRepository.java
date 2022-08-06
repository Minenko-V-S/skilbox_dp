package org.example.BlogEngine.repository;

import org.example.BlogEngine.model.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {
    @Query ("FROM Posts p ORDER BY p.timestamp DESC")
    Page<Posts> getRecentPosts (PageRequest pageRequest);

    @Query ("FROM Posts p ORDER BY SIZE(p.postComments) DESC")
    Page<Posts> getPopularPosts(PageRequest pageRequest);

    @Query("FROM Posts p WHERE p.isActive = 1 ORDER BY p.postLikes.size DESC")
    Page<Posts> getBestPosts(PageRequest pageRequest);

    @Query ("FROM Posts p ORDER BY p.timestamp")
    Page<Posts> getEarlyPosts(PageRequest pageRequest);

    @Query("FROM Posts p WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED'")
    Collection<Posts> findAllActivePosts ();

    @Query("FROM Posts p WHERE p.userId = ?1")
    Collection<Posts> findAllPostsByUserId (int userId);

    List<Posts> findByTextContaining(String text);
}
