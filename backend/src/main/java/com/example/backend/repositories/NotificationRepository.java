package com.example.backend.repositories;

import com.example.backend.models.entities.Notificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notificate, Long> {
}
