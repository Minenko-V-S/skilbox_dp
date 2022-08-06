package org.example.BlogEngine.repository;

import org.example.BlogEngine.model.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCodes, Integer> {
    Optional<CaptchaCodes> findBySecretCode (String secretCode);
}