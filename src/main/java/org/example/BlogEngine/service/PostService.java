package org.example.BlogEngine.service;

import org.example.BlogEngine.model.Posts;
import org.example.BlogEngine.model.User;
import org.example.BlogEngine.repository.CommentRepository;
import org.example.BlogEngine.repository.PostRepository;
import org.example.BlogEngine.repository.PostVoteRepository;
import org.example.BlogEngine.repository.UserRepository;
import org.example.BlogEngine.response.GeneralResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    private final CommentRepository commentRepository;
    private final PostVoteRepository postVoteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(CommentRepository commentRepository, PostVoteRepository postVoteRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postVoteRepository = postVoteRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        if (offset > limit) {
            return new ResponseEntity<>("Wrong input parameters!", HttpStatus.BAD_REQUEST);
        }

        List<Posts> post = getOrderedPosts(offset, limit, mode);
        List<Map<String, Object>> postMapList = new ArrayList<>();

        for (Posts post1 : post) {
            Map<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("id", post1.getPostId());
            responseMap.put("timestamp", post1.getTimestamp().getTime() / 1000);
            int userId = post1.getUserId();
            User user = userRepository.getOne(userId);
            Map<String, Object> userMap = new LinkedHashMap<>();
            userMap.put("id", userId);
            userMap.put("name", user.getName());
            responseMap.put("user", userMap);
            responseMap.put("title", post1.getTitle());
            responseMap.put("announce", post1.getAnnounce().replaceAll("<(.*?)>", "").replaceAll("[\\p{P}\\p{S}]", ""));
            responseMap.put("commentCount", commentRepository.getCommentCountByPostId(post1.getPostId()));
            responseMap.put("viewCount", post1.getViewCount());
            postMapList.add(responseMap);
        }

        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setCount(post.size());
        generalResponse.setPosts(getOffsetLimitOutput(postMapList, offset, limit));
        return ResponseEntity.ok(generalResponse);
    }

    private List<Posts> getOrderedPosts(Integer offset, Integer limit, String mode) {
        Page<Posts> posts;
        PageRequest pageRequest = PageRequest.of(offset / limit, limit);
        switch (mode) {
            case "early":
                posts = postRepository.getEarlyPosts(pageRequest);
                break;
            default:
                posts = postRepository.getRecentPosts(pageRequest);
                break;
        }
        return posts.toList();


    }
    private List<Map<String, Object>> getOffsetLimitOutput(List<Map<String, Object>> list,
                                                           Integer offset, Integer limit) {
        List<Map<String, Object>> listResult;
        if (offset > list.size() || offset > limit) {
            return new ArrayList<>();
        }
        if (limit + offset <= list.size()) {
            listResult = list.subList(offset, offset + limit);
        } else {
            listResult = list.subList(offset, list.size());
        }
        return listResult;
    }
}
