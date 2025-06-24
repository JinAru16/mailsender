package com.maru.tools.email.repository;


import com.maru.tools.email.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findByDiscordId(String id);
}


