package org.example.BlogEngine.repository;

import org.example.BlogEngine.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository  extends JpaRepository <Session, Integer> {
}
