package ru.core.profilems.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.core.profilems.aop.annotation.HasPermission;
import ru.core.profilems.aop.annotation.HasRole;
import ru.core.profilems.aop.annotation.CheckProfileOwnership;
import ru.core.profilems.dto.ProfileDto;
import ru.core.profilems.dto.Role;
import ru.core.profilems.dto.request.SearchParameters;
import ru.core.profilems.dto.response.PageResponse;
import ru.core.profilems.mapper.ProfileMapper;
import ru.core.profilems.model.Profile;
import ru.core.profilems.service.ProfileService;
import ru.core.profilems.validation.OnCreate;
import ru.core.profilems.validation.OnUpdate;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
public class ProfileControllerImpl implements ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @GetMapping
    @HasRole(Role.ROLE_ADMIN)
    @Override
    public ResponseEntity<PageResponse<ProfileDto>> getProfiles(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size
    ) {
        Page<Profile> pageEntity = profileService.getAllProfiles(page, size);
        var response = toPageResponse(pageEntity, profileMapper::toDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search")
    @HasRole(Role.ROLE_ADMIN)
    @Override
    public ResponseEntity<PageResponse<ProfileDto>> searchProfiles(
            @RequestParam("query") String query,
            @RequestParam(value = "ignoreCase", required = false, defaultValue = "false") boolean ignoreCase,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size
    ) {
        var searchParams = SearchParameters.builder()
                .query(query)
                .ignoreCase(ignoreCase)
                .page(page)
                .size(size)
                .build();

        Page<Profile> pageEntity = profileService.search(searchParams);
        var response = toPageResponse(pageEntity, profileMapper::toDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{profileId}")
    @HasPermission
    @Override
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable("profileId") UUID profileId) {
        var profile = profileService.getProfile(profileId);
        var profileDto = profileMapper.toDto(profile);

        return ResponseEntity.ok().body(profileDto);
    }

    @PostMapping
    @Override
    public ResponseEntity<ProfileDto> createProfile(
            @RequestBody @Validated(OnCreate.class) ProfileDto profileDto) {
        var profile = profileMapper.toEntity(profileDto);
        profile = profileService.create(profile);

        return ResponseEntity
                .created(URI.create("/api/v1/profile/" + profile.getProfileId()))
                .body(profileMapper.toDto(profile));
    }

    @PutMapping("/{profileId}")
    @CheckProfileOwnership
    @Override
    public ResponseEntity<ProfileDto> updateProfile(
            @PathVariable(name = "profileId") UUID profileId,
            @RequestBody @Validated(OnUpdate.class) ProfileDto profileDto) {
        var profile = profileMapper.toEntity(profileDto);
        profile = profileService.update(profileId, profile);

        return ResponseEntity.ok().body(profileMapper.toDto(profile));
    }

    @DeleteMapping("/{profileId}")
    @CheckProfileOwnership
    @Override
    public ResponseEntity<HttpStatus> deleteProfile(@PathVariable UUID profileId) {
        profileService.delete(profileId);
        return ResponseEntity.noContent().build();
    }

    private <T, R> PageResponse<R> toPageResponse(Page<T> pageEntity, Function<T, R> mapper) {
        List<R> profiles = pageEntity.getContent().stream().map(mapper).toList();

        return PageResponse.<R>builder()
                .content(profiles)
                .totalPages(pageEntity.getTotalPages())
                .totalElements(pageEntity.getTotalElements())
                .curPage(pageEntity.getNumber() + 1)
                .pageSize(pageEntity.getSize())
                .build();
    }
}
