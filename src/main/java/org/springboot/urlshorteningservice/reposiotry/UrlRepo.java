package org.springboot.urlshorteningservice.reposiotry;

import org.springboot.urlshorteningservice.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepo extends JpaRepository<Url, String> {
    Optional<Url> findByShortCode(String shortCode);
}
