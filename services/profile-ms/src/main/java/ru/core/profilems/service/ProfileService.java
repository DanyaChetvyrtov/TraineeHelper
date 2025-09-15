package ru.core.profilems.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.core.profilems.dto.CurrentUser;
import ru.core.profilems.dto.request.SearchParameters;
import ru.core.profilems.exception.exception.PageNotFound;
import ru.core.profilems.exception.exception.ProfileNotFoundException;
import ru.core.profilems.model.Profile;
import ru.core.profilems.repository.ProfileRepository;

import java.util.UUID;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final CurrentUser currentUser;

    public Page<Profile> getAllProfiles(Integer page, Integer size) {
        var pageEntity = profileRepository.findAll(PageRequest.of(page - 1, size));

        if (pageEntity.getTotalPages() < page) throw new PageNotFound("Such page does not exist");

        return pageEntity;
    }

    public Page<Profile> search(SearchParameters searchParameters) {
        BiFunction<String, PageRequest, Page<Profile>> method = searchParameters.isIgnoreCase() ?
                profileRepository::searchAnywhereInNameOrSurnameIgnoreCase :
                profileRepository::searchAnywhereInNameOrSurname;

        var pageEntity = method.apply(searchParameters.getQuery(), searchParameters.getPageRequest());
        if (pageEntity.getTotalPages() < searchParameters.getPage()) throw new PageNotFound("Such page does not exist");

        return pageEntity;
    }

    public Profile getProfile(UUID profileId) {
        return profileRepository.findById(profileId).orElseThrow(ProfileNotFoundException::new);
    }

    @Transactional
    public Profile create(Profile profile) {
        profile.setProfileId(UUID.fromString(currentUser.getUserId()));
        if(profileRepository.existsById(profile.getProfileId()))
            throw new IllegalArgumentException("Profile with ID " + profile.getProfileId() + " already exists");

        profile.setProfileId(profile.getProfileId());

        return profileRepository.save(profile);
    }

    @Transactional
    public Profile update(UUID profileId, Profile profile) {
        if (!profileId.equals(profile.getProfileId()))
            throw new IllegalArgumentException("ID in path and body must match");

        var existedProfile = getProfile(profileId);

        existedProfile.setName(profile.getName());
        existedProfile.setSurname(profile.getSurname());
        existedProfile.setAge(profile.getAge());

        return profileRepository.save(existedProfile);
    }

    @Transactional
    public void delete(UUID profileId) {
        profileRepository.deleteById(profileId);
    }
}
