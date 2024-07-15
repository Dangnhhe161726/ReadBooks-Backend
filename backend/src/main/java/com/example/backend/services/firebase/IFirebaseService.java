package com.example.backend.services.firebase;

import com.example.backend.models.dtos.NotificattionDTO;

import java.util.concurrent.CompletableFuture;

public interface IFirebaseService {

    CompletableFuture<Boolean> createNotification(NotificattionDTO notification);
}
