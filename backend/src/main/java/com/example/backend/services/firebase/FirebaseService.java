package com.example.backend.services.firebase;

import com.example.backend.models.dtos.NotificattionDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FirebaseService implements IFirebaseService {

    private final DatabaseReference reference;

    public FirebaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        this.reference = firebaseDatabase.getReference("Notification");
    }

    @Override
    public CompletableFuture<Boolean> createNotification(NotificattionDTO notification) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        reference.push().setValue(notification, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                future.complete(true);
            }
        });

        return future;
    }
}
