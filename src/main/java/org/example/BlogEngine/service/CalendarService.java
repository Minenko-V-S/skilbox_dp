package org.example.BlogEngine.service;

import org.example.BlogEngine.model.Posts;
import org.example.BlogEngine.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private final PostRepository postRepository;

    public CalendarService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public ResponseEntity<?> getApiCalendar(Integer year) {
        List<Integer> years;
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        List<Timestamp> timestamps;
        Map<String, Object> responseMap = new LinkedHashMap<>();
        Map<String, Integer> posts = new LinkedHashMap<>();
        int postCountAtDate;
        List<Posts> postsList = postRepository.findAll().stream() // instead of .findAllActivePosts ()
                .sorted(Comparator.comparing(Posts::getTimestamp))
                .collect(Collectors.toList());
        years = postsList.stream()
                .map(p -> convertTimeToYear(p.getTimestamp()))
                .distinct()
                .collect(Collectors.toList());
        if (year > 1970 && year <= convertTimeToYear(currentTimestamp)) {
            timestamps = postRepository.findAll().stream()
                    .map(Posts::getTimestamp)
                    .filter(t_stamp -> convertTimeToYear(t_stamp).equals(year))
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            int currentYear = LocalDate.now().getYear();
            timestamps = postsList.stream().
                    map(Posts::getTimestamp).
                    filter(t_stamp -> convertTimeToYear(t_stamp).equals(currentYear)).
                    distinct().
                    collect(Collectors.toList());
        }
        timestamps.sort(Comparator.naturalOrder());
        for (Timestamp d : timestamps) {
            postCountAtDate = (int) postsList.stream()
                    .filter(p -> (p.getTimestamp().toInstant()
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()).equals(d.toInstant()
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()))
                    .count();
            posts.put(String.valueOf(d.toInstant()
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDate()), postCountAtDate);
        }
        responseMap.put("years", years);
        responseMap.put("posts", posts);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    public Integer convertTimeToYear(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        cal.setTimeInMillis(time.getTime());
        String curTime = String.valueOf(cal.get(Calendar.YEAR));
        return Integer.parseInt(curTime);
    }
}
