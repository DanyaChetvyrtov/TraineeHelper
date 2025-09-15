package ru.core.profilems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.core.profilems.dto.CurrentUser;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    @Bean
    public CurrentUser currentUser() {
        return new CurrentUser();
    }
}
