package org.springboot.urlshorteningservice.repository;

import org.springboot.urlshorteningservice.model.UrlClicks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlClickRepo extends JpaRepository<UrlClicks, String> {}
