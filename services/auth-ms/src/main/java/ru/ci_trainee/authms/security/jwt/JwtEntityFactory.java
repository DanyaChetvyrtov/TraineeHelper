package ru.ci_trainee.authms.security.jwt;

import ru.ci_trainee.authms.model.User;

public class JwtEntityFactory {


    public static JwtEntity create(User user) {
        return new JwtEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }
}
