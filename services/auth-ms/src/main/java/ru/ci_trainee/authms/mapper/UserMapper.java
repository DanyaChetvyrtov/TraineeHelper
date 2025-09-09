package ru.ci_trainee.authms.mapper;

import org.mapstruct.Mapper;
import ru.ci_trainee.authms.dto.UserDto;
import ru.ci_trainee.authms.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userWriteDto);

}