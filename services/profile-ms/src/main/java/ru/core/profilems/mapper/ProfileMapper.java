package ru.core.profilems.mapper;

import org.mapstruct.Mapper;
import ru.core.profilems.dto.ProfileDto;
import ru.core.profilems.model.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDto toDto(Profile profile);

    Profile toEntity(ProfileDto profileDto);
}
