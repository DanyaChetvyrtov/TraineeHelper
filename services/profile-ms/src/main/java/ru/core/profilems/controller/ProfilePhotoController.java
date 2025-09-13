package ru.core.profilems.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.core.profilems.service.PhotoStorageService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/profile/photo")
@RequiredArgsConstructor
public class ProfilePhotoController {

    private final PhotoStorageService photoStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadPhoto(
            @RequestParam UUID profileId,
            @RequestParam("photo") MultipartFile photo
    ) throws Exception {
        log.info("Received photo upload: profileId={}, photoSize={}", profileId, photo.getSize());
        photoStorageService.uploadPhoto(profileId.toString(), photo);
    }

    @GetMapping(value = "/{profileId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPhoto(@PathVariable UUID profileId) throws Exception {
        log.info("Photo for user with ID: profileId={}", profileId);
        return photoStorageService.downloadPhoto(profileId.toString());
    }

    @DeleteMapping
    public void deletePhoto(@RequestParam UUID profileId) throws Exception {
        log.info("Deleting photo ID: profileId={}", profileId);
        photoStorageService.deletePhoto(profileId.toString());
    }
}
