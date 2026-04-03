package org.springboot.urlshorteningservice.repository;

import jakarta.transaction.Transactional;
import org.springboot.urlshorteningservice.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UrlRepo extends JpaRepository<Url, String> {
    Optional<Url> findByShortCode(String shortCode);

    @Modifying
    @Transactional
    @Query("DELETE FROM url u WHERE u.createdAt < :cutoff")
    void deleteByCreatedAtBefore(@Param("cutoff") LocalDateTime cutoff);
}
