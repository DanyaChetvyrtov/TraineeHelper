package ru.core.profilems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import ru.core.profilems.dto.ProfileDto;
import ru.core.profilems.dto.response.PageResponse;

import java.util.UUID;

@Tag(name = "Profile API", description = "Profile endpoints")
public interface ProfileController {
    @Operation(
            summary = "Receive all profiles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved profiles"),
                    @ApiResponse(responseCode = "400", description = "Invalid parameters")
            }
    )
    ResponseEntity<PageResponse<ProfileDto>> getProfiles(Integer page, Integer size);

    @Operation(
            summary = "Search profiles by query(name or surname)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved profiles"),
                    @ApiResponse(responseCode = "404", description = "Profiles not found")
            }
    )
    ResponseEntity<PageResponse<ProfileDto>> searchProfiles(
            String query, boolean ignoreCase, Integer page, Integer size);

    @Operation(
            summary = "Receive profile and its Tasks by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved profile"),
                    @ApiResponse(responseCode = "404", description = "Profile not found")
            }
    )
    ResponseEntity<ProfileDto> getProfileById(UUID profileId);

    @Operation(
            summary = "Create new profile",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created profile"),
                    @ApiResponse(responseCode = "400", description = "ID should not be specified"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    ResponseEntity<ProfileDto> createProfile(ProfileDto profileDto);

    @Operation(
            summary = "Update profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated profile"),
                    @ApiResponse(responseCode = "400", description = "ID mismatch"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    ResponseEntity<ProfileDto> updateProfile(UUID profileId, ProfileDto profileDto);

    @Operation(
            summary = "Update profile",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted profile")
            }
    )
    ResponseEntity<HttpStatus> deleteProfile(@PathVariable UUID profileId);
}
