package org.example.BlogEngine.repository;

import org.example.BlogEngine.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {
    @Query(value = "FROM Tags t WHERE t.name = ?1")
    Optional<Tags> findTagByName(String name);
}
