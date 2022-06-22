package org.example.BlogEngine.service;

import org.example.BlogEngine.model.Posts;
import org.example.BlogEngine.model.Tags;
import org.example.BlogEngine.repository.PostRepository;
import org.example.BlogEngine.repository.TagsRepository;
import org.example.BlogEngine.response.TagResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagsService {

    private final TagsRepository tagsRepository;
    private final PostRepository postRepository;

    public TagsService(TagsRepository tagsRepository, PostRepository postRepository) {
        this.tagsRepository = tagsRepository;
        this.postRepository = postRepository;
    }

    public ResponseEntity<?> getTag(String query) {
        Map<String, List<TagResponse>> tagsResponseMap;
        List<String> tagsList;
        if (query.contains(",")) {
            tagsList = List.of(query.split(",")); //Разбиваем строку запроса на теги по запятым
            List<String> tagsCleaned = tagsList.stream().map(String::trim).collect(Collectors.toList());
            tagsResponseMap = getTagResponsesMap(tagsCleaned);
        } else {
            tagsList = List.of(query);
            tagsResponseMap = getTagResponsesMap(tagsList);
        }
        return new ResponseEntity<>(tagsResponseMap, HttpStatus.OK);
    }
    public ResponseEntity<?> getTag() {
        List<Tags> tags = tagsRepository.findAll();
        List<String> tagNames = tags.stream().map(Tags::getName).collect(Collectors.toList());
        Map<String, List<TagResponse>> tagsMap = getTagResponsesMap(tagNames);
        return new ResponseEntity<>(tagsMap, HttpStatus.OK);
    }
    private Map<String, List<TagResponse>> getTagResponsesMap(List<String> tagNameList) {
        List<Posts> postList = (List<Posts>) postRepository.findAllActivePosts();
        var count = getCount();
        List<Integer> postsPerTagList = new ArrayList<>();
        for (String t : tagNameList) {
            postsPerTagList.add((int) postList.stream().filter(p -> p.getText().contains(t)).count());
        }
        int maxPostsPerTag = postsPerTagList.stream().max(Comparator.naturalOrder()).orElse(count);
        List<Double> partialWeights = postsPerTagList.stream().map(t -> (double) t / maxPostsPerTag)
                .collect(Collectors.toList());
        List<TagResponse> tagResponseList = new ArrayList<>();
        for (int i = 0; i < partialWeights.size(); i++) {
            tagResponseList.add(new TagResponse(tagNameList.get(i), partialWeights.get(i)));
        }
        return Map.of("tags", tagResponseList);
    }

    public Integer getCount() {
        int count;
        try {
            List<Posts> postList = (List<Posts>) postRepository.findAllActivePosts();
            count = postList.size();
        } catch (NullPointerException ex) {
            count = 0;
        }
        return count;
    }

}
