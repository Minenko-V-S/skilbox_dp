package org.example.BlogEngine.service;

import org.example.BlogEngine.enums.ModerationStatus;
import org.example.BlogEngine.model.*;
import org.example.BlogEngine.repository.*;
import org.example.BlogEngine.requests.CommentRequest;
import org.example.BlogEngine.requests.LikeRequest;
import org.example.BlogEngine.response.GeneralResponse;
import org.example.BlogEngine.response.ResultResponse;
import org.example.BlogEngine.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class PostService {
    private final CommentRepository commentRepository;
    private final PostVoteRepository postVoteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagsRepository tagsRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final AuthService authService;
    private ResponseEntity<?> responseEntity;

    public PostService(CommentRepository commentRepository, PostVoteRepository postVoteRepository, UserRepository userRepository, PostRepository postRepository, TagsRepository tagsRepository, Tag2PostRepository tag2PostRepository, AuthService authService) {
        this.commentRepository = commentRepository;
        this.postVoteRepository = postVoteRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagsRepository = tagsRepository;
        this.tag2PostRepository = tag2PostRepository;
        this.authService = authService;
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

    public ResponseEntity<?> getPostsBySearch(Integer offset, Integer limit, String query) {
        if (offset > limit) {
            return new ResponseEntity<>("Wrong input parameters!", HttpStatus.BAD_REQUEST);
        }

        GeneralResponse generalResponse = new GeneralResponse();
        List<Map<String, Object>> postMapList = new ArrayList<>();
        List<Posts> posts = postRepository.findByTextContaining(query.trim());
        int count = posts.size();
        if (count == 0) {
            return new ResponseEntity<>("No posts with this text!", HttpStatus.NOT_FOUND);
        }

        posts.forEach(post -> {
            Map<String, Object> responseMap = new LinkedHashMap<>();
            Map<String, Object> userMap = new LinkedHashMap<>();
            responseMap.put("id", post.getPostId());
            responseMap.put("timestamp", post.getTimestamp().getTime() / 1000);
            Optional<User> userOptional = userRepository.findById(post.getUserId());
            userOptional.ifPresent(user -> userMap.put("id", user.getUserId()));
            userOptional.ifPresent(user -> userMap.put("name", user.getName()));
            responseMap.put("user", userMap);
            responseMap.put("title", post.getTitle());
            responseMap.put("announce", post.getAnnounce());
            responseMap.put("likeCount", likeCount(post));
            responseMap.put("dislikeCount", likeCount(post));
            responseMap.put("commentCount", commentRepository.getCommentCountByPostId(post.getPostId()));
            responseMap.put("viewCount", post.getViewCount());
            postMapList.add(responseMap);
        });
        generalResponse.setCount(count);
        generalResponse.setPosts(getOffsetLimitOutput(postMapList, offset, limit));
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> getPostsByDate(Integer offset, Integer limit, String date) {
        if (offset > limit) {
            return new ResponseEntity<>("Wrong input parameters!", HttpStatus.BAD_REQUEST);
        }

        var posts = postRepository.findAllActivePosts();
        int count = 0;
        List<Map<String, Object>> postMapList = new ArrayList<>();
        for (Posts post : posts) {
            if (post.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()
                    .equals(date)) {
                count++;
                Map<String, Object> responseMap = getMapOfPostElements(post);
                Map<String, Object> userMap = new LinkedHashMap<>();
                Optional<User> userOptional = userRepository.findById(post.getUserId());
                userOptional.ifPresent(user -> userMap.put("name", user.getName()));
                userOptional.ifPresent(user -> userMap.put("id", user.getUserId()));
                responseMap.put("user", userMap);
                postMapList.add(responseMap);
            }
        }
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setCount(count);
        generalResponse.setPosts(getOffsetLimitOutput(postMapList, offset, limit));
        responseEntity = new ResponseEntity<>(generalResponse, HttpStatus.OK);
        return responseEntity;
    }

    private Map<String, Object> getMapOfPostElements(Posts post) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("id", post.getPostId());
        responseMap.put("timestamp", post.getTimestamp().getTime() / 1000);
        responseMap.put("title", post.getTitle());
        responseMap.put("announce", post.getAnnounce());
        responseMap.put("likeCount", likeCount(post));
        responseMap.put("dislikeCount", dislikeCount(post));
        responseMap.put("commentCount", commentRepository.getCommentCountByPostId(post.getPostId()));
        responseMap.put("viewCount", post.getViewCount());
        return responseMap;
    }

    public ResponseEntity<?> getPostsByTag(Integer offset, Integer limit, String tagName) {
        if (offset > limit) {
            return new ResponseEntity<>("Wrong input parameters!", HttpStatus.BAD_REQUEST);
        }
        Optional<Tags> tagOptional = tagsRepository.findTagByName(tagName);
        if (tagOptional.isEmpty()) {
            responseEntity = new ResponseEntity<>("No tag " + tagName + " is registered.",
                    HttpStatus.NO_CONTENT);
        } else {
//            responseEntity =  getPostsBySearch(offset, limit, tagName);
            Tags tag = tagOptional.get();
            int tagId = tag.getId();
            List<Posts> posts = new ArrayList<>();
            List<Integer> postsIdList = tag2PostRepository.findPostIdByTagId(tagId);
            postsIdList.forEach(id -> posts.add(postRepository.getOne(id))
            );
            List<Map<String, Object>> postMapList = new ArrayList<>();
            for (Posts post : posts) {
                Map<String, Object> responseMap = getMapOfPostElements(post);
                Map<String, Object> userMap = new LinkedHashMap<>();
                userMap.put("id", post.getUserId());
                Optional<User> userOptional = userRepository.findById(post.getUserId());
                userOptional.ifPresent(user -> userMap.put("name", user.getName()));
                responseMap.put("user", userMap);
                postMapList.add(responseMap);
            }
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setCount(posts.size());
            generalResponse.setPosts(getOffsetLimitOutput(postMapList, offset, limit));
            responseEntity = new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostById(Integer postId) {
        if (postRepository.findById(postId).isEmpty()) {
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> commentsMapList = new ArrayList<>();
        Map<String, Object> responseMap = new LinkedHashMap<>();
        var post = postRepository.getOne(postId);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        Map<String, Object> userMap = new LinkedHashMap<>();
        responseMap.put("id", post.getPostId());
        responseMap.put("timestamp", post.getTimestamp().getTime() / 1000);
        responseMap.put("active", post.getIsActive());

        User user = userRepository.getOne(post.getUserId());
        userMap.put("name", user.getName());
        userMap.put("id", user.getUserId());

        responseMap.put("user", userMap);
        responseMap.put("title", post.getTitle());
        responseMap.put("announce", post.getAnnounce());
        responseMap.put("text", post.getText());
        responseMap.put("likeCount", likeCount(post));
        responseMap.put("dislikeCount", dislikeCount(post));
        responseMap.put("viewCount", post.getViewCount());

        List<PostComments> postCommentList = commentRepository.findCommentsByPostId(postId);
        postCommentList.forEach(c -> {
            Map<String, Object> commentMap = new LinkedHashMap<>();
            commentMap.put("id", c.getCommentId());
            commentMap.put("timestamp", c.getTime().getTime() / 1000);
            commentMap.put("text", c.getText());
            User commentUser = userRepository.findById(c.getUserId()).orElseThrow();
            UserResponse userResponse = new UserResponse();
            userResponse.setId(commentUser.getUserId());
            userResponse.setName(commentUser.getName());
            userResponse.setPhoto(commentUser.getPhoto());
            commentMap.put("user", userResponse);
            commentsMapList.add(commentMap);
            responseMap.put("comments", commentsMapList);
        });
        if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                post.getTimestamp().getTime() < Timestamp.valueOf(LocalDateTime.now()).getTime()) {
            var tagsIdList = tag2PostRepository.findTagIdsByPostId(postId);
            List<String> tagNames = new ArrayList<>();

            if (!tagsIdList.isEmpty()) {
                tagNames = tagsIdList.stream()
                        .map(t -> tagsRepository.getOne(t).getName())
                        .collect(Collectors.toList());
            }
            responseMap.put("tags", tagNames);
        }
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    public ResponseEntity<?> postLikeDislike(LikeRequest likeRequest, Integer value) {
        PostVotes postVote;
        int userId = authService.getUserId();
        int post_id = likeRequest.getPost_id();
        Posts post = postRepository.getOne(likeRequest.getPost_id());
        Optional<PostVotes> postVoteOptional = postVoteRepository.getOneByPostAndUser(post_id, userId);
        if (!authService.isUserAuthorized()) {
            return ResponseEntity.ok(new ResultResponse(false));
        }

        if (post.getUserId().equals(userId)) {
            return ResponseEntity.ok(new ResultResponse(false));
        }

        if (postVoteOptional.isPresent()) {
            if (postVoteOptional.get().getValue().equals(value)) {
                return new ResponseEntity<>("This vote is doubled! Not acceptable.", HttpStatus.OK);
            } else {
                postVote = postVoteOptional.get();
                postVote.setValue(value);
                postVoteRepository.save(postVote);
                return new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
            }
        }
        PostVotes newPostVote = createPostVote(post_id, value, userId);
        postVoteRepository.save(newPostVote);
        return new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
    }

    private PostVotes createPostVote(Integer postId, Integer value, Integer userId) {
        PostVotes postVote = new PostVotes();
        postVote.setPost(postRepository.getOne(postId));
        postVote.setPostId(postId);
        postVote.setTime(Timestamp.valueOf(now()));
        postVote.setUserId(userId);
        postVote.setValue(value);
        return postVote;
    }

    public ResponseEntity<?> postComment(CommentRequest commentRequest) {
        boolean result = true;
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Map<String, String> errors = new LinkedHashMap<>();
        if (!authService.isUserAuthorized()) {
            errors.put("errors", "User is unauthorized!");
            map.put("result", new ResultResponse(false));
            map.put("errors", errors);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        Integer userId = authService.getUserId();
        Integer postId = commentRequest.getPost_id();
        PostComments postComment = new PostComments();
        if (commentRequest.getText().length() < 10 || commentRequest.getText().length() > 300) {
            result = false;
            errors.put("text", "Text's length is out of limit!");
        }
        if (result) {
            if (commentRequest.getParent_id() != null) {
                postComment.setParent_id(commentRequest.getParent_id());
            }
            postComment.setPost_id(postId);
            postComment.setPost(postRepository.getOne(postId));
            postComment.setText(commentRequest.getText());
            postComment.setTime(Timestamp.valueOf(now()));
            postComment.setUserId(userId);
            commentRepository.save(postComment);
            map.put("id", postComment.getCommentId());
        } else {
            map.put("result", new ResultResponse(false));
            map.put("errors", errors);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    private Integer likeCount(Posts post) {
        return postVoteRepository.findCountVotesByPostId(post.getPostId(), 1).orElse(0);
    }

    private Integer dislikeCount(Posts post) {
        return postVoteRepository.findCountVotesByPostId(post.getPostId(), -1).orElse(0);
    }
}
