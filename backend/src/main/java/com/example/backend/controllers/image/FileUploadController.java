package com.example.backend.controllers.image;

import com.example.backend.models.dtos.NotificattionDTO;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.amazons3.IAmazonS3Service;
import com.example.backend.services.book.IBookService;
import com.example.backend.services.cloudinary.ICloudinaryService;
import com.example.backend.services.firebase.IFirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class FileUploadController {

    private final ICloudinaryService cloudinaryService;
    private final IAmazonS3Service amazonS3Service;
    private final IBookService bookService;
    private final IFirebaseService firebaseService;
    private String timeStamp = LocalDateTime.now().toString();

    @PostMapping("/upload-to-cloud-dinary/{bookId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<String>> uploadFiles(
            @RequestParam("file") MultipartFile file) {
        List<String> urls = new ArrayList<>();
        try {
            String url = cloudinaryService.uploadFile(file);
            urls.add(url);
            return new ResponseEntity<>(urls, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-to-amazon/{bookId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file) {
        try {
            String fileName = amazonS3Service.uploadFile(file);
            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-by-book/{bookId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> uploadFileByBookId(
            @PathVariable("bookId") Long id,
            @RequestParam("thumnail") MultipartFile fileThumnail,
            @RequestParam("fileBook") MultipartFile fileBook) {
        try {
            String url = cloudinaryService.uploadFile(fileThumnail);
            String fileName = amazonS3Service.uploadFile(fileBook);
            BookResponse bookResponse = bookService.createLinkAndThumnail(id, url, fileName);
            NotificattionDTO notificattion = new NotificattionDTO();
            notificattion.setBookId(bookResponse.getId());
            notificattion.setCreateTime(timeStamp);
            notificattion.setLinkBook(bookResponse.getThumbnail());
            notificattion.setTitle(bookResponse.getName());
            notificattion.setDescription(bookResponse.getIntroduce());
            CompletableFuture<Boolean> future = firebaseService.createNotification(notificattion);
            String mess = future.get() ? "Notification created successfully." : "Notification created unsuccessfully.";

            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Insert file to book success. " + mess)
                            .data(Map.of("Book", bookResponse))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .timeStamp(timeStamp)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/download-from-amazon")
    @PreAuthorize("hasAnyAuthority('READER', 'ADMIN')")
    public ResponseEntity<HttpResponse> getFileUrl(
            @RequestParam("fileName") String fileName
    ) {
        String url = amazonS3Service.getFileUrl(fileName);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Get url file book success")
                        .data(Map.of("url", url))
                        .build()
        );
    }
}